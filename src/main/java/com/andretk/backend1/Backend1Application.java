package com.andretk.backend1;

import com.andretk.backend1.entities.User;
import com.andretk.backend1.repositories.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.time.Instant;

@SpringBootApplication
public class Backend1Application {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Backend1Application.class, args);
        var repository = context.getBean(UserRepository.class);
    }

}
