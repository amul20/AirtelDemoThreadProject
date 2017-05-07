package org.airtel.project.di;

import org.airtel.project.controller.TEAMController;
import org.springframework.context.annotation.Bean;

import javax.inject.Singleton;

/**
 * Created by amulyam on 06/05/17.
 */
public class ServerDI {
    @Bean
    @Singleton
    public TEAMController teamController(){
        return new TEAMController();
    }
}
