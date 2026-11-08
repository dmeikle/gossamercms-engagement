package com.gossamercms.chat.config;

import com.gossamercms.mvc.autoconfig.*;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.autoconfigure.AutoConfiguration;

@AutoConfiguration
@EnableModuleModels(basePackage = "com.gossamercms.chat.models")
@EnableModuleConverters(basePackage = "com.gossamercms.chat.converters")
@EnableModuleDbServices(basePackage = "com.gossamercms.chat.data")
@EnableModuleHandlers(basePackage = "com.gossamercms.chat.handlers")
@EnableModuleServices(basePackage = "com.gossamercms.chat.services")
@EnableModuleProviders(basePackage = "com.gossamercms.chat.providers")
@EnableModuleEventListeners(basePackage = "com.gossamercms.chat.listeners")
public class ChatAutoConfig {

    @PostConstruct
    public void init() {
        System.out.println("************************************************************");
        System.out.println(">>> ChatAutoConfig LOADED");
        System.out.println("************************************************************");
    }

}
