package com.zes.squad.gmh.web.task;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.CollectionUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.zes.squad.gmh.cache.CacheService;
import com.zes.squad.gmh.common.util.DateUtils;
import com.zes.squad.gmh.common.util.JsonUtils;
import com.zes.squad.gmh.entity.po.AppointmentPo;
import com.zes.squad.gmh.entity.union.AppointmentProjectUnion;
import com.zes.squad.gmh.entity.union.AppointmentUnion;
import com.zes.squad.gmh.entity.union.UserUnion;
import com.zes.squad.gmh.helper.SMSHelper;
import com.zes.squad.gmh.lock.RedisLock;
import com.zes.squad.gmh.property.MessageProperties;
import com.zes.squad.gmh.service.AppointmentService;
import com.zes.squad.gmh.service.UserService;
import com.zes.squad.gmh.web.common.JsonResults;
import com.zes.squad.gmh.web.websocket.WebSocketServer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AppointmentTask {

    private static final String CACHE_KET_APPOINTMENT_PREFIX = "_cache_key_appointment_%s";

    @Autowired
    private UserService         userService;
    @Autowired
    private AppointmentService  appointmentService;
    @Autowired
    private RedisLock           redisLock;
    @Autowired
    private CacheService        cacheService;

    public void remindFrontDesk() {
        log.info(">>>>>预约信息提示前台定时任务开始运行>>>>>");
        List<UserUnion> onlineUsers = userService.listOnLineUsers();
        if (CollectionUtils.isEmpty(onlineUsers)) {
            log.info("当前无登录用户");
            return;
        }
        log.info("当前登录用户数:{}", onlineUsers.size());
        List<AppointmentUnion> unions = appointmentService.getRemindAppointment();
        WebSocketServer webSocketServer = new WebSocketServer();
        for (UserUnion user : onlineUsers) {
            String token = user.getUserTokenPo().getToken();
            List<AppointmentUnion> appointmentUnions = Lists.newArrayList();
            for (AppointmentUnion appointmentUnion : unions) {
                if (appointmentUnion == null || appointmentUnion.getStoreUnion() == null
                        || appointmentUnion.getStoreUnion().getStorePo() == null) {
                    continue;
                }
                Long appointmentStoreId = appointmentUnion.getStoreUnion().getStorePo().getId();
                Long userStoreId = user.getUserPo().getStoreId();
                log.info("appointmentStoreId is {}, userStoreId is {}", appointmentStoreId, userStoreId);
                if (Objects.equals(appointmentStoreId, userStoreId)) {
                    appointmentUnions.add(appointmentUnion);
                }
            }
            String message = JsonUtils.toJson(JsonResults.success(appointmentUnions));
            webSocketServer.sendMessage(token, message);
        }
        log.info(">>>>>预约信息提示前台定时任务结束运行>>>>>");
    }

    public void remindCustomers() {
        log.info(">>>>>预约信息提示用户定时任务开始运行>>>>>");
        //使用redis分布式锁保证多台机器同时执行定时任务每个预约短信只发送一次
        RedissonClient client = redisLock.getRedissonClient();
        if (client == null) {
            log.error(">>>>>redis锁初始化失败");
            return;
        }
        List<AppointmentUnion> unions = appointmentService.getRemindAppointment();
        for (AppointmentUnion appointmentUnion : unions) {
            if (appointmentUnion == null || appointmentUnion.getAppointmentPo() == null) {
                log.warn("预约信息为空");
                continue;
            }
            AppointmentPo appointmentPo = appointmentUnion.getAppointmentPo();
            if (appointmentPo.getId() == null || Strings.isNullOrEmpty(appointmentPo.getCustomerMobile())) {
                log.warn("预约信息缺失");
                continue;
            }
            if (CollectionUtils.isEmpty(appointmentUnion.getAppointmentProjects())) {
                log.warn("预约项目为空");
                continue;
            }
            String appointmentMessage = "";
            List<AppointmentProjectUnion> projects = appointmentUnion.getAppointmentProjects();
            for (int i = 0; i < projects.size(); i++) {
                AppointmentProjectUnion project = projects.get(i);
                if (project == null) {
                    log.warn("预约项目为空, appointment id is {}", appointmentPo.getId());
                    continue;
                }
                if (Strings.isNullOrEmpty(project.getProjectName()) || project.getBeginTime() == null) {
                    log.warn("预约项目信息缺失, appointment id is {}", appointmentPo.getId());
                    continue;
                }
                if (i == 0) {
                    appointmentMessage += DateUtils.format(project.getBeginTime(), "M月d日H时m分") + " "
                            + project.getProjectName();
                } else {
                    appointmentMessage += "," + project.getProjectName();
                }
            }
            if (!Strings.isNullOrEmpty(appointmentMessage)) {
                //以预约id作为缓存key唯一标识
                String cacheKey = String.format(CACHE_KET_APPOINTMENT_PREFIX,
                        appointmentPo.getId() + appointmentPo.getCustomerMobile());
                String mobile = cacheService.get(cacheKey);
                if (Strings.isNullOrEmpty(mobile)) {
                    //缓存信息不存在说明这个预约没处理过
                    RLock lock = client.getLock(String.format(CACHE_KET_APPOINTMENT_PREFIX, appointmentPo.getId()));
                    try {
                        if (lock.tryLock(5, TimeUnit.SECONDS)) {
                            //尝试获取锁,获取成功后执行发送逻辑,失败释放锁,5秒的参数防止死锁,如果到了5秒无论如何都会释放(并不是说到了5秒才释放)
                            boolean result = SMSHelper.sendMessage(appointmentPo.getCustomerMobile(),
                                    MessageProperties.get("template.appointment"), appointmentMessage);
                            if (!result) {
                                log.error("验证码发送失败, appointment id is {}, mobile is {}", appointmentPo.getId(),
                                        appointmentPo.getCustomerMobile());
                                continue;
                            }
                            cacheService.put(cacheKey, appointmentPo.getCustomerMobile());
                        }
                    } catch (InterruptedException e) {
                        log.error("使用redis锁控制预约提醒异常", e);
                    } finally {
                        if (lock != null) {
                            //手动释放锁
                            lock.unlock();
                        }
                    }
                }
            }
        }
        log.info(">>>>>预约信息提示用户定时任务结束运行>>>>>");
    }

}
