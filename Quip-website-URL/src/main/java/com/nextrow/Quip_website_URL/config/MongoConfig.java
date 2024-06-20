package com.nextrow.Quip_website_URL.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

@Configuration
public class MongoConfig {

    @Bean
    public MongoTemplate mongoTemplate(){
        MongoClient mongoClient= MongoClients.create();
        Logger logger= LoggerFactory.getLogger(MongoConfig.class);
        logger.info("Database instance created successfully.");
        return new MongoTemplate(new SimpleMongoClientDatabaseFactory(mongoClient,"domains"));
    }

}