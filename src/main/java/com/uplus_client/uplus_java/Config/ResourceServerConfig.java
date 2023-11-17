package com.uplus_client.uplus_java.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Value("${spring.security.user.authorities.role_1}")
    private String role_1;

    @Override
    public void configure(HttpSecurity http) throws Exception {

        // For using h2-console on your web browser. You need to disable csrf and header frameOptions.
        http.csrf().disable();
        http.headers().frameOptions().disable();

        // You can configurate which path are they need token.
        http
                .authorizeRequests()
                .antMatchers("/uplus_example/client/order").hasRole(role_1)
                .and().httpBasic();
    }
}
