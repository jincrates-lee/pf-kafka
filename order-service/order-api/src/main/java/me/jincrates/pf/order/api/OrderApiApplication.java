package me.jincrates.pf.order.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = "me.jincrates.pf.order.dataacess")
@EnableJpaRepositories(basePackages = "me.jincrates.pf.order.dataacess")
@ComponentScan(basePackages = "me.jincrates.pf")
@SpringBootApplication
public class OrderApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApiApplication.class, args);
    }
}