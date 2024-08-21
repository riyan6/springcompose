package org.spring.compose.common.utils;

import cn.hutool.core.lang.Assert;
import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.NonNull;
import org.spring.compose.common.model.user.AuthUser;

public class UserUtils {

    public static class CurrentUser {
        private static final TransmittableThreadLocal<AuthUser> cache = new TransmittableThreadLocal<>();

        public static void set(@NonNull AuthUser user) {
            cache.set(user);
        }

        public static AuthUser get() {
            AuthUser authUser = cache.get();
            Assert.notNull(authUser, "当前用户为空");
            return authUser;
        }

        public static long id() {
            return get().getId();
        }
    }

}
