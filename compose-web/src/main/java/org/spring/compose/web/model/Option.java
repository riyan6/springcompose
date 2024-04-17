package org.spring.compose.web.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "下拉框对象")
@Data
@NoArgsConstructor
public class Option<T> {

    @Schema(description = "选项的值")
    private T value;

    @Schema(description = "选项的标签")
    private String label;

    @Schema(description = "子选项列表")
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    private List<Option> children;

}
