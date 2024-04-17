package org.spring.compose.common.out.result;

import lombok.Data;
import org.spring.compose.common.enums.BaseEnum;
import org.spring.compose.common.enums.CodeEnum;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {

    private Integer code;

    private T data;

    private String msg;

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMsg(ResultCode.SUCCESS.getMsg());
        result.setData(data);
        return result;
    }

    public static <T> Result<T> failed() {
        return result(ResultCode.EXCEPTION.getCode(), ResultCode.EXCEPTION.getMsg(), null);
    }

    public static <T> Result<T> failed(String msg) {
        return result(ResultCode.EXCEPTION.getCode(), msg, null);
    }

    public static <T> Result<T> judge(boolean status) {
        if (status) {
            return success();
        } else {
            return failed();
        }
    }

    public static <T> Result<T> failed(CodeEnum codeEnum) {
        return result(codeEnum.getCode(), codeEnum.getMsg(), null);
    }

    public static <T> Result<T> failed(CodeEnum codeEnum, String msg) {
        return result(codeEnum.getCode(), msg, null);
    }

    private static <T> Result<T> result(CodeEnum codeEnum, T data) {
        return result(codeEnum.getCode(), codeEnum.getMsg(), data);
    }

    private static <T> Result<T> result(Integer code, String msg, T data) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setData(data);
        result.setMsg(msg);
        return result;
    }

    public static boolean isSuccess(Result<?> result) {
        return result != null && ResultCode.SUCCESS.getCode().equals(result.getCode());
    }
}
