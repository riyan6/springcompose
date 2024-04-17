package org.spring.compose.web.config;

import cn.hutool.core.lang.Console;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

public class ComposeWebEnvPostProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Properties props = new Properties();
        props.put("spring.main.keep-alive", true);
        props.put("spring.threads.virtual.enabled", true);
        props.put("server.tomcat.threads.max", 800);
        props.put("server.tomcat.threads.min-spare", 100);
        props.put("server.tomcat.accept-count", 1000);

        Enumeration<?> enumeration = props.propertyNames();
        while (enumeration.hasMoreElements()) {
            String key = (String) enumeration.nextElement();
            Console.error("配置项", key, props.get(key));
        }

        environment.getPropertySources().addLast(new PropertiesPropertySource("vt-config", props));
    }

}
