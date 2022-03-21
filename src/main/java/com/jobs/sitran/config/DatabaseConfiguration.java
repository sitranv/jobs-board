package com.jobs.sitran.config;

import com.github.mongobee.Mongobee;
import com.mongodb.client.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
@EnableMongoRepositories("com.jobs.sitran.repository")
//@Import(value = MongoAutoConfiguration.class)
//@EnableMongoAuditing(auditorAwareRef = "springSecurityAuditorAware")
public class DatabaseConfiguration {

    @Value("${spring.data.mongodb.database}")
    private String database;

    private final Logger log = LoggerFactory.getLogger(DatabaseConfiguration.class);

//    @Bean
//    public Mongobee mongobee(){
//        Mongobee runner = new Mongobee("mongodb://localhost:27017/jobs-board");
//        runner.setDbName("jobs-board");         // host must be set if not set in URI
//        runner.setChangeLogsScanPackage( "com.jobs.sitran.config.dbmigration"); // the package to be scanned for changesets
//
//        return runner;
//    }
}
