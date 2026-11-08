package com.gossamercms.contact.config;

import com.gossamercms.mvc.autoconfig.*;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.autoconfigure.AutoConfiguration;

@AutoConfiguration
@EnableModuleModels(basePackage = "com.gossamercms.contact.models")
@EnableModuleConverters(basePackage = "com.gossamercms.contact.converters")
@EnableModuleDbServices(basePackage = "com.gossamercms.contact.data")
@EnableModuleHandlers(basePackage = "com.gossamercms.contact.handlers")
@EnableModuleServices(basePackage = "com.gossamercms.contact.services")
@EnableModuleProviders(basePackage = "com.gossamercms.contact.providers")
@EnableModuleEventListeners(basePackage = "com.gossamercms.contact.listeners")
public class ContactAutoConfig {

    @PostConstruct
    public void init() {
        System.out.println("************************************************************");
        System.out.println(">>> ContactAutoConfig LOADED");
        System.out.println("************************************************************");
    }

}
