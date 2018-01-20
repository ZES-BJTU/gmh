package com.zes.squad.gmh.task.service;

public interface AppointmentTaskService {

    /**
     * 定时任务提醒客户预约信息
     */
    void remindCustomer();

    /**
     * 定时任务提醒前台预约信息
     */
    void remindReception();

}
