package com.zes.squad.gmh.context;

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
    
        public static UserPo getUser() {
            return userContext.get();
        }
    
        public static void removeUser() {
            userContext.remove();
        }

}
