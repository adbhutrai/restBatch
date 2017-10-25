package com.example.demo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

public class LoggingStudentWriter implements ItemWriter<Student> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingStudentWriter.class);

    @Override
    public void write(List<? extends Student> items) throws Exception {
        LOGGER.info("Received the information of {} students", items.size());

        items.forEach(i -> LOGGER.debug("Received the information of a student: {}", i));
    }
}