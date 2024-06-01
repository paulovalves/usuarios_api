package com.teste.vm_tecnologia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication()
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class VmTecnologiaApplication {

    public static void main(String[] args) {
        SpringApplication.run(VmTecnologiaApplication.class, args);
    }

}
