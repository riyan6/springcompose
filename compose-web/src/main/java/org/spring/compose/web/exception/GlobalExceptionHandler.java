package org.spring.compose.web.exception;

import cn.hutool.core.util.StrUtil;
import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.spring.compose.common.exception.BizException;
import org.spring.compose.common.out.result.Result;
import org.spring.compose.common.out.result.ResultCode;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.sql.SQLSyntaxErrorException;
import java.util.concurrent.CompletionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(BindException.class)
//    public <T> Result<T> processException(BindException e) {
//        log.error("BindException:{}", e.getMessage());
//        String msg = e.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining("；"));
//        return Result.failed(ResultCode.PARAM_ERROR, msg);
//    }

//    /**
//     * RequestParam参数的校验
//     *
//     * @param e
//     * @param <T>
//     * @return
//     */
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(ConstraintViolationException.class)
//    public <T> Result<T> processException(ConstraintViolationException e) {
//        log.error("ConstraintViolationException:{}", e.getMessage());
//        String msg = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining("；"));
//        return Result.failed(ResultCode.PARAM_ERROR, msg);
//    }

//    /**
//     * RequestBody参数的校验
//     *
//     * @param e
//     * @param <T>
//     * @return
//     */
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public <T> Result<T> processException(MethodArgumentNotValidException e) {
//        log.error("MethodArgumentNotValidException:{}", e.getMessage());
//        String msg = e.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining("；"));
//        return Result.failed(ResultCode.PARAM_ERROR, msg);
//    }

//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    @ExceptionHandler(NoHandlerFoundException.class)
//    public <T> Result<T> processException(NoHandlerFoundException e) {
//        log.error(e.getMessage(), e);
//        return Result.failed(ResultCode.RESOURCE_NOT_FOUND);
//    }

//    /**
//     * MissingServletRequestParameterException
//     */
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(MissingServletRequestParameterException.class)
//    public <T> Result<T> processException(MissingServletRequestParameterException e) {
//        log.error(e.getMessage(), e);
//        return Result.failed(ResultCode.PARAM_IS_NULL);
//    }

//    /**
//     * MethodArgumentTypeMismatchException
//     */
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
//    public <T> Result<T> processException(MethodArgumentTypeMismatchException e) {
//        log.error(e.getMessage(), e);
//        return Result.failed(ResultCode.PARAM_ERROR, "类型错误");
//    }

    /**
     * ServletException
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServletException.class)
    public <T> Result<T> processException(ServletException e) {
        log.error(e.getMessage(), e);
        return Result.failed(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public <T> Result<T> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("非法参数异常，异常原因：{}", e.getMessage(), e);
        return Result.failed(e.getMessage());
    }

    /**
     * HttpMessageNotReadableException
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public <T> Result<T> processException(HttpMessageNotReadableException e) {
        log.error(e.getMessage(), e);
        String errorMessage = "请求体不可为空";
        Throwable cause = e.getCause();
        if (cause != null) {
            errorMessage = convertMessage(cause);
        }
        return Result.failed(errorMessage);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TypeMismatchException.class)
    public <T> Result<T> processException(TypeMismatchException e) {
        log.error(e.getMessage(), e);
        return Result.failed(e.getMessage());
    }

//    @ResponseStatus(HttpStatus.FORBIDDEN)
//    @ExceptionHandler(SQLSyntaxErrorException.class)
//    public <T> Result<T> processSQLSyntaxErrorException(SQLSyntaxErrorException e) {
//        String errorMsg = e.getMessage();
//        log.error(errorMsg);
//        if (StrUtil.isNotBlank(errorMsg) && errorMsg.contains("denied to user")) {
//            return Result.failed(ResultCode.FORBIDDEN_OPERATION);
//        } else {
//            return Result.failed(e.getMessage());
//        }
//    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CompletionException.class)
    public <T> Result<T> processException(CompletionException e) {
        if (e.getMessage().startsWith("feign.FeignException")) {
            return Result.failed("微服务调用异常");
        }
        return handleException(e);
    }

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(FeignException.BadRequest.class)
//    public <T> Result<T> processException(FeignException.BadRequest e) {
//        log.info("微服务feign调用异常:{}", e.getMessage());
//        return Result.failed(e.getMessage());
//    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public <T> Result<T> processException(HttpRequestMethodNotSupportedException e) {
        log.info("请求资源不存在:{}", e.getMessage());
        return Result.failed(ResultCode.NOT_FOUND);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BizException.class)
    public <T> Result<T> handleBizException(BizException e) {
        log.error("biz exception:{}", e.getMessage(), e);
        if (e.getResultCode() != null) {
            return Result.failed(e.getResultCode());
        }
        return Result.failed(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public <T> Result<T> handleException(Exception e) {
        log.error("unknown exception:{}", e.getMessage(), e);
        String errorMsg = e.getMessage();
        if (StrUtil.isNotBlank(errorMsg) && errorMsg.contains("denied to user")) {
            return Result.failed(ResultCode.FORBIDDEN_OPERATION);
        }
        if (StrUtil.isBlank(errorMsg)) {
            errorMsg = "系统异常";
        }
        return Result.failed(errorMsg);
    }

    /**
     * 传参类型错误时，用于消息转换
     *
     * @param throwable 异常
     * @return 错误信息
     */
    private String convertMessage(Throwable throwable) {
        String error = throwable.toString();
        String regulation = "\\[\"(.*?)\"]+";
        Pattern pattern = Pattern.compile(regulation);
        Matcher matcher = pattern.matcher(error);
        String group = "";
        if (matcher.find()) {
            String matchString = matcher.group();
            matchString = matchString.replace("[", "").replace("]", "");
            matchString = matchString.replaceAll("\\\"", "") + "字段类型错误";
            group += matchString;
        }
        return group;
    }

}
