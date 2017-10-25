package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class RestbatchApplication {
	
	public static void main(String[] args) {
SpringApplication.run(RestbatchApplication.class, args);

		/*RestTemplate restTemplate = new RestTemplate();
		Student[] quotes = restTemplate.getForObject(
				"http://localhost:8801/api/v1/demo/students", Student[].class);
		for (Student student : quotes) {
			System.out.println(student);
		} */
	}
}
