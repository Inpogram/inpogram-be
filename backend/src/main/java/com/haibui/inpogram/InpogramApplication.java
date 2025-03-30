package com.haibui.inpogram;

import com.haibui.inpogram.configurations.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class)
public class InpogramApplication {

    public static void main(String[] args) {
        SpringApplication.run(InpogramApplication.class, args);
    }

}
