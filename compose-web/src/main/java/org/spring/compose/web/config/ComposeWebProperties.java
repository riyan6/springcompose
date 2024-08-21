package org.spring.compose.web.config;

import cn.hutool.core.collection.CollUtil;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashSet;
import java.util.Set;

@Data
@ConfigurationProperties("compose.web")
public class ComposeWebProperties {

    private Set<String> urlWhiteList;

    @PostConstruct
    void init() {
        if (CollUtil.isEmpty(urlWhiteList)) {
            urlWhiteList = new HashSet<>();
        }
        urlWhiteList.add("/doc.html");
        urlWhiteList.add("/favicon.ico");
        urlWhiteList.add("/webjars/js/**");
        urlWhiteList.add("/webjars/css/**");
        urlWhiteList.add("/v3/api-docs/**");
        urlWhiteList.add("favicon.ico.");
    }

}
