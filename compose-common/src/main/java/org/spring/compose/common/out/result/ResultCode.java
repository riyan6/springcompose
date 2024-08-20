package org.spring.compose.common.out.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.spring.compose.common.enums.CodeEnum;

@Getter
@AllArgsConstructor
public enum ResultCode implements CodeEnum {

    SUCCESS(0, "处理成功"),
    EXCEPTION(-1, "业务处理失败"),

    FORBIDDEN_OPERATION(403, "非法操作"),
    NOT_FOUND(404, "资源不存在"),

    REPEAT_SUBMIT_ERROR(10001, "您的请求已提交，请不要重复提交或等待片刻再尝试。");

    private final Integer code;
    private final String msg;



}
