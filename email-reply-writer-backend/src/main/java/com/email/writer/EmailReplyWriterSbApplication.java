package com.email.writer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class EmailReplyWriterSbApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmailReplyWriterSbApplication.class, args);
	}
}