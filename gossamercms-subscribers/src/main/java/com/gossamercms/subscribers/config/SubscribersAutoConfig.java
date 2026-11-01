package com.gossamercms.subscribers.config;

import com.gossamercms.mvc.autoconfig.*;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.autoconfigure.AutoConfiguration;

@AutoConfiguration
@EnableModuleModels(basePackage = "com.gossamercms.subscribers.models")
@EnableModuleConverters(basePackage = "com.gossamercms.subscribers.converters")
@EnableModuleDbServices(basePackage = "com.gossamercms.subscribers.data")
@EnableModuleHandlers(basePackage = "com.gossamercms.subscribers.handlers")
@EnableModuleServices(basePackage = "com.gossamercms.subscribers.services")
@EnableModuleProviders(basePackage = "com.gossamercms.subscribers.providers")
@EnableModuleEventListeners(basePackage = "com.gossamercms.subscribers.listeners")
public class SubscribersAutoConfig {

    @PostConstruct
    public void init() {
        System.out.println("************************************************************");
        System.out.println(">>> SubscribersAutoConfig LOADED");
        System.out.println("************************************************************");
    }

}
