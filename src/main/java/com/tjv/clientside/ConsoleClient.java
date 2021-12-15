package com.tjv.clientside;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ConsoleClient {
    public static void main(String[] args) {
        var app = new SpringApplication(ConsoleClient.class);
        app.setLogStartupInfo(false);
        app.setWebApplicationType(WebApplicationType.NONE);
        app.run(args);
    }
}
