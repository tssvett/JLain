package dev.tssvett.schedule_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ScheduleBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScheduleBotApplication.class, args);
    }
}
