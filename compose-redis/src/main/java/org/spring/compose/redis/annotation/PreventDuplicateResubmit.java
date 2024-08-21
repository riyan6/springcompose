package org.spring.compose.redis.annotation;

import org.spring.compose.common.enums.RequestMethodEnum;

import java.lang.annotation.*;

/**
 * 禁止重复请求注解
 */
@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PreventDuplicateResubmit {

    /**
     * 重复提交间隔
     *
     * @return 默认5秒
     */
    int expire() default 5;

    /**
     * 请求类型 可选值 get、post、delete、put
     *
     * @return 默认 get
     */
    RequestMethodEnum method() default RequestMethodEnum.GET;

    /**
     * 方法名
     *
     * @return 方法名称
     */
    String methodName();

}
