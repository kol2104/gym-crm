package com.epam.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSslConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "spring.activemq.user")
public class ActiveMQConnectionConfiguration {

    @Value("${spring.activemq.broker-url}")
    private String brokerUrl;
    @Value("${spring.activemq.user}")
    private String user;
    @Value("${spring.activemq.password}")
    private String password;

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQSslConnectionFactory(brokerUrl);
        activeMQConnectionFactory.setTrustAllPackages(true);
        if ("".equals(user)) {
            return activeMQConnectionFactory;
        }
        activeMQConnectionFactory.setUserName(user);
        activeMQConnectionFactory.setPassword(password);
        return activeMQConnectionFactory;
    }
}
