package io.batch.springbatch.batch.job.api;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.batch.springbatch.batch.listener.JobListener;
import io.batch.springbatch.batch.tasklet.ApiEndTasklet;
import io.batch.springbatch.batch.tasklet.ApiStartTasklet;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ApiJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ApiStartTasklet apiStartTasklet;
    private final ApiEndTasklet apiEndTasklet;
    private final Step jobStep;

    @Bean
    public Job apiJob(){ // api 요청에 대한 Job수행
        return jobBuilderFactory.get("apiJob")
            .listener(new JobListener())
            .start(apiStep1())
            .next(jobStep)
            .next(apiStep2())
            .build();
    }

    @Bean
    public Step apiStep1() {
        return stepBuilderFactory.get("apiStep1")
            .tasklet(apiStartTasklet)
            .build();
    }

    @Bean
    public Step apiStep2() {
        return stepBuilderFactory.get("apiStep2")
            .tasklet(apiEndTasklet)
            .build();
    }
    
}
