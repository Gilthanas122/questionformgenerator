package com.bottomupquestionphd.demo.testconfiguration;

import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;
import com.bottomupquestionphd.demo.domains.dtos.appuser.LoginDTO;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class TestConfigurationBeanFactory {

    @Bean(name = {"validUser"})
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    AppUser getPlayerWithValidCredentials() {
        AppUser fakePlayer = new AppUser("validUser", "Geeks@portal20", "ROLE_USER");
        return fakePlayer;
    }

    @Bean(name = "validLoginDTO")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    LoginDTO getValidLoginDTO() {
        LoginDTO loginDTO = new LoginDTO("validUser", "Geeks@portal20");
        return loginDTO;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
