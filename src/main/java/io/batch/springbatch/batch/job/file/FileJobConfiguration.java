package io.batch.springbatch.batch.job.file;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import io.batch.springbatch.batch.chunk.processor.FileItemProcessor;
import io.batch.springbatch.batch.model.domain.Product;
import io.batch.springbatch.batch.model.dto.ProductVO;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class FileJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public Job fileJob(){
        return jobBuilderFactory.get("fileJob")
            .start(fileStep1())
            .build();
    }

    @Bean
    public Step fileStep1() {
        return stepBuilderFactory.get("fileStep1")
            .<ProductVO, Product>chunk(10)
            .reader(fileItemReader(null))
            .processor(fileItemProcessor())
            .writer(fileItemWriter())
            .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<ProductVO> fileItemReader( // FlatFile은 csv 같은 파일을 파싱
        @Value("#{jobParameters['requestDate']}") String requestDate
    ) {
        return new FlatFileItemReaderBuilder<ProductVO>()
            .name("flatfile")
            .resource(new ClassPathResource("product_" + requestDate + ".csv")) // 참조할 소스데이터
            .targetType(ProductVO.class) // 매핑할 객체
            .linesToSkip(1) // 첫번째 라인은 스킵
            .delimited().delimiter(",") // 구분자 지정
            .names("id", "name", "price", "type") // 타이틀 이름
            .build();
    }

    @Bean
    public ItemProcessor<ProductVO, Product> fileItemProcessor() {
        return new FileItemProcessor();
    }

    @Bean
    public ItemWriter<? super Product> fileItemWriter() {
        return new JpaItemWriterBuilder<Product>()
            .entityManagerFactory(entityManagerFactory)
            .usePersist(true) // default true (Merge vs Persist)
            .build();
    }


    
}
