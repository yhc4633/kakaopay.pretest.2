package com.kakaopay.pretest;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Slf4j
@Aspect
@EnableAspectJAutoProxy
@SpringBootApplication
public class StartApplicationServer {
    public static void main(String[] args) {
        try {
            // spring-boot 기동
            ConfigurableApplicationContext context = SpringApplication.run(StartApplicationServer.class, args);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            log.error("Kakaopay Pretest 2 Server Startup Failed.");
        }
    }
}