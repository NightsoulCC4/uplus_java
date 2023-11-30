package com.uplus_client.uplus_java.Config;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Value("${security.oauth2.client.client-id}")
    private String client_id;

    @Value("${security.oauth2.client.client-secret}")
    private String client_secret;

    @Value("${security.oauth2.client.authorized-grant-types.password}")
    private String password;

    @Value("${security.oauth2.client.authorized-grant-types.authorization_code}")
    private String authoization_code;

    @Value("${security.oauth2.client.authorized-grant-types.refresh_token}")
    private String refresh_token;

    @Value("${security.oauth2.client.authorized-grant-types.client_credentials}")
    private String client_credentials;

    @Value("${security.oauth2.client.scope.read}")
    private String read;

    @Value("${security.oauth2.client.scope.write}")
    private String write;

    @Value("${security.oauth2.client.access-token-validity-seconds}")
    private int accessTokenValiditySeconds;

    @Value("${spring.security.user.name.client_1}")
    private String user_1;

    @Value("${spring.security.user.password.client_1}")
    private String password_1;

    @Value("${spring.security.user.authorities.role_1}")
    private String role_1;

    private AuthenticationManager authenticationManager;

    // Constructor.
    @Autowired
    public AuthorizationServerConfig(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    // Configure the endpoint.
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.authenticationManager(authenticationManager);
    }

    // Grant token access.
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                .allowFormAuthenticationForClients();
    }

    // Configure token for each user for store in memory.
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
          .withUser(user_1).password(passwordEncoder().encode(password_1)).roles(role_1);
    }

    // Configure the client-id and client-secret to generate token.
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // If you want to encode the client_secret, you can use passwordEncoder() function nor insert "{noop}" at the front.
        clients.inMemory()
                .withClient(client_id)
                .secret(passwordEncoder().encode(client_secret))
                .authorizedGrantTypes(password, authoization_code, refresh_token)
                .scopes(read, write)
                .accessTokenValiditySeconds(accessTokenValiditySeconds);
    }

    // Encode the password from text to md-5.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
