package com.example.demo;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableBatchProcessing

public class StudentJobConfig {

    private static final String PROPERTY_REST_API_URL = "rest.api.to.database.job.api.url";
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Bean
    ItemReader<Student> restStudentReader(Environment environment, RestTemplate restTemplate) {
        return new StudentReader(environment.getRequiredProperty(PROPERTY_REST_API_URL), restTemplate);
    }
    @Bean
    public ResourcelessTransactionManager transactionManager() {
        return new ResourcelessTransactionManager();
    }

    @Bean
    public JobRepository jobRepository(ResourcelessTransactionManager transactionManager) throws Exception {
        MapJobRepositoryFactoryBean mapJobRepositoryFactoryBean = new MapJobRepositoryFactoryBean(transactionManager);
        mapJobRepositoryFactoryBean.setTransactionManager(transactionManager);
        return mapJobRepositoryFactoryBean.getObject();
    }

    @Bean
    public SimpleJobLauncher jobLauncher(JobRepository jobRepository) {
        SimpleJobLauncher simpleJobLauncher = new SimpleJobLauncher();
        simpleJobLauncher.setJobRepository(jobRepository);
        return simpleJobLauncher;
    }
    @Bean
    ItemProcessor<Student, Student> restStudentProcessor() {
        return new LoggingStudentProcessor();
    }

    @Bean
    ItemWriter<Student> restStudentWriter() {
        return new LoggingStudentWriter();
    }

    @Bean
    Step restStudentStep(ItemReader<Student> restStudentReader,
                         ItemProcessor<Student, Student> restStudentProcessor,
                         ItemWriter<Student> restStudentWriter,
                         StepBuilderFactory stepBuilderFactory) {
        return stepBuilderFactory.get("restStudentStep")
                .<Student, Student>chunk(1)
                .reader(restStudentReader)
                .processor(restStudentProcessor)
                .writer(restStudentWriter)
                .build();
    }

    @Bean
    Job restStudentJob(JobBuilderFactory jobBuilderFactory,
                       @Qualifier("restStudentStep") Step restStudentStep) {
        return jobBuilderFactory.get("restStudentJob")
                .incrementer(new RunIdIncrementer())
                .flow(restStudentStep)
                .end()
                .build();
    }
}
