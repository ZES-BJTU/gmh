package com.zes.squad.gmh.context;

import static com.zes.squad.gmh.common.helper.LogicHelper.ensureEntityExist;

import com.zes.squad.gmh.entity.po.UserPo;

public abstract class ThreadContext {

    private static ThreadLocal<UserPo> userContext = new ThreadLocal<UserPo>() {
        @Override
        protected UserPo initialValue() {
            return new UserPo();
        }
    };

    public static void setUser(UserPo po) {
        userContext.set(po);
    }

    public static UserPo getCurrentUser() {
        UserPo po = userContext.get();
        ensureEntityExist(po, "获取当前用户信息异常");
        return po;
    }

    public static Long getUserStoreId() {
        UserPo po = userContext.get();
        return po.getStoreId();
    }

    public static void removeUser() {
        userContext.remove();
    }

}
