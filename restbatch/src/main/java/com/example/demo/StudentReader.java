package com.example.demo;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class StudentReader implements ItemReader<Student> {
	private static final Logger LOGGER = LoggerFactory.getLogger(StudentReader.class);

    private final String apiUrl;
    private final RestTemplate restTemplate;

    private int nextStudentIndex;
    private List<Student> studentData;

    StudentReader(String apiUrl, RestTemplate restTemplate) {
        this.apiUrl = apiUrl;
        this.restTemplate = restTemplate;
        nextStudentIndex = 0;
    }

    @Override
    public Student read() throws Exception {
        LOGGER.info("Reading the information of the next student");

        if (studentDataIsNotInitialized()) {
            studentData = fetchStudentDataFromAPI();
        }

        Student nextStudent = null;

        if (nextStudentIndex < studentData.size()) {
            nextStudent = studentData.get(nextStudentIndex);
            nextStudentIndex++;
        }

        LOGGER.info("Found student: {}", nextStudent);

        return nextStudent;
    }

    private boolean studentDataIsNotInitialized() {
        return this.studentData == null;
    }

    private List<Student> fetchStudentDataFromAPI() {
        LOGGER.debug("Fetching student data from an external API by using the url: {}", apiUrl);

        ResponseEntity<Student[]> response = restTemplate.getForEntity(apiUrl, Student[].class);
        Student[] studentData = response.getBody();
        LOGGER.debug("Found {} students", studentData.length);

        return Arrays.asList(studentData);
    }
}
