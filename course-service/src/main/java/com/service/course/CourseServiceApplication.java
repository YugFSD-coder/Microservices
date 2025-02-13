package com.service.course;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CourseServiceApplication {

	private static final Logger log = LoggerFactory.getLogger(CourseServiceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(CourseServiceApplication.class, args);
	}



}
