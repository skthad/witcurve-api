package com.witcurve.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfiguration {

    @Autowired
    ApplicationProperties applicationProperties;

    @Bean
    public BasicAWSCredentials basicAWSCredentials() {
        return new BasicAWSCredentials(applicationProperties.getAws().getAccessKey(), applicationProperties.getAws().getSecretKey());
    }

    @Bean
    public AmazonS3 amazonS3Client() {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(applicationProperties.getAws().getAccessKey(), applicationProperties.getAws().getSecretKey());
        return AmazonS3ClientBuilder.standard()
            .withRegion(Regions.fromName(applicationProperties.getAws().getDefaultRegion()))
            .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
            .build();
    }


    @Bean
    public TransferManager amazonTransferManager(AmazonS3 amazonS3Client) {
        return TransferManagerBuilder.standard().withS3Client(amazonS3Client).build();
    }

}
