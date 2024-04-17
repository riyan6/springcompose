package org.spring.compose.web.util;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.NonNull;
import org.spring.compose.common.model.user.AuthUser;
import org.springframework.util.Assert;

public class UserUtil {

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
