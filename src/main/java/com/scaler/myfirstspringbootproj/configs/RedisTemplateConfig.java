//package com.scaler.myfirstspringbootproj.configs;
//
//import com.scaler.myfirstspringbootproj.models.Product;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
//import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//@Configuration
//public class RedisTemplateConfig {
//
//    @Bean
//    public RedisTemplate<String, Product> redisTemplate(
//            RedisConnectionFactory connectionFactory) {
//
//        RedisTemplate<String, Product> redisTemplate =
//                new RedisTemplate<>();
//
//        redisTemplate.setConnectionFactory(connectionFactory);
//
//        Jackson2JsonRedisSerializer<Product> serializer =
//                new Jackson2JsonRedisSerializer<>(Product.class);
//
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(serializer);
//
//        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//        redisTemplate.setHashValueSerializer(serializer);
//
//        redisTemplate.afterPropertiesSet();
//
//        return redisTemplate;
//    }
//}
