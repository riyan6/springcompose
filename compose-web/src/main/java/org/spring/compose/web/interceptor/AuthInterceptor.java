package org.spring.compose.web.interceptor;

import cn.hutool.core.text.AntPathMatcher;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.spring.compose.common.constants.OrderConstant;
import org.spring.compose.common.exception.BizException;
import org.spring.compose.common.utils.UserUtils;
import org.spring.compose.web.annotation.IgnoreAuthorization;
import org.spring.compose.common.model.user.AuthUser;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.lang.reflect.Method;

@Slf4j
@ConditionalOnExpression("${compose.web.authInterceptorEnable:false}")
@Order(OrderConstant.AUTH_INTERCEPTOR_ORDER)
public class AuthInterceptor implements HandlerInterceptor, WebMvcConfigurer {

    private final String ERROR_URL = "/error";
    private final String ACCESS_TOKEN_HEADER = "Authorization";
    private final String[] whiteList = {"/doc.html", "/favicon.ico", "/webjars/js/**", "/webjars/css/**", "/v3/api-docs/**"};
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求路径
        String path = getPath(request);

        log.info("请求路径：%s".formatted(path));

        // 如果方法类型是 option 或者是错误链接 就放行
        if (HttpMethod.OPTIONS.name().equals(request.getMethod()) || path.equals(ERROR_URL)) {
            return HandlerInterceptor.super.preHandle(request, response, handler);
        }

        // 检查是否白名单
        for (String url : whiteList) {
            if (antPathMatcher.match(url, path)) {
                return HandlerInterceptor.super.preHandle(request, response, handler);
            }
        }

        // 检查是否有跳过验证的注解
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            IgnoreAuthorization ignoreAuthentication = method.getAnnotation(IgnoreAuthorization.class);
            if (ignoreAuthentication != null) {
                return HandlerInterceptor.super.preHandle(request, response, handler);
            }
        }

        // 获取token
        String tokenHeader = request.getHeader(ACCESS_TOKEN_HEADER);
        // 如果token为空就抛出异常
        if (StrUtil.isBlank(tokenHeader)) {
            throw new BizException("无效授权用户");
        }
        // 解析tokenHeader 即jwt解析出用户数据并存储
        AuthUser user = AuthUser.builder().build();
        UserUtils.CurrentUser.set(user);

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this).addPathPatterns("/**");
    }

    /**
     * 获取请求的路径地址
     *
     * @param request http请求对象
     * @return
     */
    private String getPath(HttpServletRequest request) {
        String path = request.getServletPath();
        if (StrUtil.isBlank(path)) {
            path = request.getRequestURI();
        }
        return path;
    }
}
