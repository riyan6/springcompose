package org.spring.compose.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Properties;

/**
 * <p>封装的SpringBoot启动类，默认配置开启虚拟线程</p>
 *
 * @author riyan6
 * @since 2024-04-17
 */
@Slf4j
public class SpringComposeWebApp implements CommandLineRunner {

    @Value("${server.port:#{'8080'}}")
    private String port;

    /**
     * 启动web程序
     *
     * @param primarySource 启动类
     * @param args          参数
     * @return
     */
    public static ConfigurableApplicationContext run(Class<?> primarySource, String... args) {
        final var app = new SpringApplication(primarySource);
        Properties props = new Properties();
        // 自定义配置 开启虚拟线程处理请求
        props.put("spring.main.keep-alive", true);
        props.put("spring.threads.virtual.enabled", true);
        props.put("server.tomcat.threads.max", 800);
        props.put("server.tomcat.threads.min-spare", 100);
        props.put("server.tomcat.accept-count", 1000);
        app.setDefaultProperties(props);
        return app.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        var docUrl = "http://127.0.0.1:%s/doc.html".formatted(this.port);
        log.info("WEB应用启动成功，API文档访问地址：{} [虚拟线程已开启]", docUrl);
    }
}
