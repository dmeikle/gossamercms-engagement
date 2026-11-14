package com.gossamercms.events.config;

import com.gossamercms.mvc.autoconfig.*;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@AutoConfiguration
@EnableConfigurationProperties(EventCheckoutProperties.class)
@EnableModuleModels(basePackage = "com.gossamercms.events.models")
@EnableModuleConverters(basePackage = "com.gossamercms.events.converters")
@EnableModuleDbServices(basePackage = "com.gossamercms.events.data")
@EnableModuleHandlers(basePackage = "com.gossamercms.events.handlers")
@EnableModuleServices(basePackage = "com.gossamercms.events.services")
@EnableModuleProviders(basePackage = "com.gossamercms.events.providers")
@EnableModuleEventListeners(basePackage = "com.gossamercms.events.listeners")
public class EventsAutoConfig {

    @PostConstruct
    public void init() {
        System.out.println("************************************************************");
        System.out.println(">>> EventsAutoConfig LOADED");
        System.out.println("************************************************************");
    }

}
