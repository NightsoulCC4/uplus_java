package com.uplus_client.uplus_java.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Value("${spring.security.user.authorities}")
    private String role;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/uplus_example/client/admit").hasRole("ADMIN")
                .antMatchers("/uplus_example/client/hello").hasRole("USER")
                .and().httpBasic();
    }


/*     @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/uplus_example/client/admit", "/uplus_example/client/discharge")
                .access("#oauth2.hasScope('read') and #oauth2.clientName == 'client1'"); // Requires authentication for path1.
    } */
}
