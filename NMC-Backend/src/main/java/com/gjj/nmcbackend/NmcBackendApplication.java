package com.gjj.nmcbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true)
@MapperScan("com.gjj.nmcbackend.mapper")
public class NmcBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(NmcBackendApplication.class , args);
    }

}
