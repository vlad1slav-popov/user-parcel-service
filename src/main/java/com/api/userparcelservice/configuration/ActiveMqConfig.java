package com.api.userparcelservice.configuration;

import com.api.userparcelservice.domain.RegisterUserRequest;
import com.api.userparcelservice.domain.UserLoginRequest;
import com.api.userparcelservice.dto.MqDTO;
import com.api.userparcelservice.entity.RoleEntity;
import com.api.userparcelservice.entity.UserEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ActiveMqConfig {

    @Bean
    MappingJackson2MessageConverter mappingJackson2MessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTypeIdPropertyName("content-type");

        Map<String, Class<?>> typeIdMapping = new HashMap<>();
        typeIdMapping.put("userLoginRequest", UserLoginRequest.class);
        typeIdMapping.put("userEntity", UserEntity.class);
        typeIdMapping.put("roles", RoleEntity.class);
        typeIdMapping.put("mqDTO", MqDTO.class);
        typeIdMapping.put("registerUserRequest", RegisterUserRequest.class);
        converter.setTypeIdMappings(typeIdMapping);
        return converter;
    }

//    @Bean
//    JmsTemplate jmsTemplate() throws JMSException {
//        JmsTemplate jmsTemplate = new JmsTemplate();
////        jmsTemplate.setDefaultDestinationName("testqueue");
////        ConnectionFactory connectionFactory = new TargetConn();
////        connectionFactory.createConnection();
////        jmsTemplate.setConnectionFactory(connectionFactory);
//        return jmsTemplate;
//    }
}
