/* package com.uplus_client.uplus_java.Config;

import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;

import org.apache.logging.log4j.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;

@Configuration
public class OAuthConfig extends AuthorizationServerConfigurerAdapter {

    @Value("${security.oauth2.client.client-id}")
    private String client_id;

    @Value("${security.oauth2.client.client-secret}")
    private String client_secret;

    @Value("${security.oauth2.client.authorized-grant-types}")
    private String grant_types;

    @Value("${security.oauth2.client.scope}")
    private String scope;

    @Value("${security.oauth2.client.access-token-validity-secords}")
    private int accessTokenValiditySeconds;

    private final static Logger logger = LogManager.getLogger(OAuthConfig.class);

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        logger.info("\n client-id: " + client_id + "\n client-secret: " + client_secret + "\n grant_types: " + grant_types + "\n scope: " + scope + "\n validateTime: " + accessTokenValiditySeconds );

        clients.inMemory()
            .withClient(client_id)
            .secret(client_secret)
            .authorizedGrantTypes(grant_types)
            .scopes(scope)
            .accessTokenValiditySeconds(accessTokenValiditySeconds);
    }
}
 */