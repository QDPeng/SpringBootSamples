package com.didispace;

import com.didispace.service.ComputeService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource({"classpath:dubbo.xml"})
public class Application {

    public static void main(String[] args) throws InterruptedException {
        ApplicationContext context = SpringApplication.run(Application.class, args);
        //获取暴露的接口
        ComputeService computeService = (ComputeService) context.getBean("computeService");
        System.out.println("computeService:" + computeService.add(111, 3333));
    }

}
