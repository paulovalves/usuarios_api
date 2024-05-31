package com.teste.vm_tecnologia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class VmTecnologiaApplication {

    public static void main(String[] args) {
        SpringApplication.run(VmTecnologiaApplication.class, args);
    }

}
