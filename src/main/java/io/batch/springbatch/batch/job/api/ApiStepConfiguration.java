package io.batch.springbatch.batch.job.api;

import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.support.ClassifierCompositeItemProcessor;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import io.batch.springbatch.batch.chunk.processor.ApiItemProcessor1;
import io.batch.springbatch.batch.chunk.processor.ApiItemProcessor2;
import io.batch.springbatch.batch.chunk.processor.ApiItemProcessor3;
import io.batch.springbatch.batch.chunk.writer.ApiItemWriter1;
import io.batch.springbatch.batch.chunk.writer.ApiItemWriter2;
import io.batch.springbatch.batch.chunk.writer.ApiItemWriter3;
import io.batch.springbatch.batch.classifier.ProcessorClassifier;
import io.batch.springbatch.batch.classifier.WriterClassifier;
import io.batch.springbatch.batch.model.dto.ApiRequestVO;
import io.batch.springbatch.batch.model.dto.ProductVO;
import io.batch.springbatch.batch.partition.ProductPartitioner;
import io.batch.springbatch.service.ApiService1;
import io.batch.springbatch.service.ApiService2;
import io.batch.springbatch.service.ApiService3;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ApiStepConfiguration {

    /*
        // 인자값 정의
        "args": [
            "job.name=apiJob",
            "requestDate=20210102"
        ]
    */
    
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;
    private final ApiService1 apiService1;
    private final ApiService2 apiService2;
    private final ApiService3 apiService3;

    private int chunkSize = 10;

    @Bean
    public Step apiMasterStep() throws Exception{
        ProductVO[] productList = QueryGenerator.getProductList(dataSource);
        return stepBuilderFactory.get("apiMasterStep")
            .partitioner(apiSlaveStep().getName(), partitioner())
            .step(apiSlaveStep())
            .gridSize(productList.length)
            .taskExecutor(taskExecutor()) // 멀티쓰레드 작업을 위한
            .build();
    }

    @Bean
    @SuppressWarnings("unchecked")
    public Step apiSlaveStep() throws Exception{
        return stepBuilderFactory.get("apislaveStep")
            .<ProductVO, ProductVO>chunk(chunkSize)  // <읽기, 쓰기>
            .reader(itemReader(null))
            .processor(itemProcessor())
            .writer(itemWriter())
            .build();
    }

    @Bean
    public ProductPartitioner partitioner(){
        ProductPartitioner productPartitioner = new ProductPartitioner();
        productPartitioner.setDataSource(dataSource);
        return productPartitioner;
    }

    @Bean
    @StepScope
    public ItemReader<ProductVO> itemReader(@Value("#{stepExecutionContext['product']}") ProductVO productVO) throws Exception {

        JdbcPagingItemReader<ProductVO> reader = new JdbcPagingItemReader<>(); // ItemReader 설정
        reader.setDataSource(dataSource);
        reader.setPageSize(chunkSize);
        reader.setRowMapper(new BeanPropertyRowMapper<>(ProductVO.class));

        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider(); // 쿼리문 작성
        queryProvider.setSelectClause("id, name, price, type");
        queryProvider.setFromClause("from product");
        queryProvider.setWhereClause("where type = :type");

        Map<String, Order> sortKeys = new HashMap<>(1); // Sorting 조건 설정
        sortKeys.put("id", Order.DESCENDING);
        queryProvider.setSortKeys(sortKeys);

        reader.setParameterValues(QueryGenerator.getParameterForQuery("type", productVO.getType())); // ItemReader에 값 넣기
        reader.setQueryProvider(queryProvider);
        reader.afterPropertiesSet();

        return reader;
    }

    @Bean
    public TaskExecutor taskExecutor() { // task를 수행할 작업자 설정
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(3); // 사용할 코어 수
        taskExecutor.setMaxPoolSize(6); // 최대 코어 수
        taskExecutor.setThreadNamePrefix("api-thread-");
        return taskExecutor;
    }

    @Bean
    @SuppressWarnings("rawtypes")
    public ItemProcessor itemProcessor() {
        // itemProcessor 보다 더 세부적으로 분류하고 처리하는 ClassifierCompositeItemProcessor
        // 1개의 item을 1개의 itemProcessor에 전달해서 하나씩 처리함
        ClassifierCompositeItemProcessor<ProductVO, ApiRequestVO> processor = new ClassifierCompositeItemProcessor<>();
        // 실제 사용할 커스텀 Classifier 구현 = ProcessorClassifier
        // 결국 Classifier는 Java 클래스에 맞게 분류해주는 틀 작업같은 느낌이다.
        ProcessorClassifier<ProductVO, ItemProcessor<?, ? extends ApiRequestVO>> classifier = new ProcessorClassifier<>();
        Map<String, ItemProcessor<ProductVO, ApiRequestVO>> processorMap = new HashMap<>();
        processorMap.put("1", new ApiItemProcessor1());
        processorMap.put("2", new ApiItemProcessor2());
        processorMap.put("3", new ApiItemProcessor3());

        classifier.setProcessorMap(processorMap);
        processor.setClassifier(classifier);
        return processor;
    }

    @Bean
    @SuppressWarnings("rawtypes")
    public ItemWriter itemWriter() {
        ClassifierCompositeItemWriter<ApiRequestVO> writer = new ClassifierCompositeItemWriter<>();
        WriterClassifier<ApiRequestVO, ItemWriter<? super ApiRequestVO>> classifier = new WriterClassifier<>();
        Map<String, ItemWriter<ApiRequestVO>> writerMap = new HashMap<>();
        writerMap.put("1", new ApiItemWriter1(apiService1));
        writerMap.put("2", new ApiItemWriter2(apiService2));
        writerMap.put("3", new ApiItemWriter3(apiService3));

        classifier.setWriterMap(writerMap);
        writer.setClassifier(classifier);
        return writer;
    }
    
}
