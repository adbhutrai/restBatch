package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class LoggingStudentProcessor implements ItemProcessor<Student, Student> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(LoggingStudentProcessor.class);

	@Override
	public Student process(Student item) throws Exception {
		LOGGER.info("Processing student information: {}", item);
		return item;
	}

}
