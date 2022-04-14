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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import io.batch.springbatch.batch.model.dto.ProductVO;
import io.batch.springbatch.batch.partition.ProductPartitioner;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ApiStepConfiguration {
    
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    private int chunkSize = 10;

    @Bean
    public Step apiMasterStep() throws Exception{
        return stepBuilderFactory.get("apiMasterStep")
            .partitioner(apiSlaveStep().getName(), partitioner())
            .step(apiSlaveStep())
            .gridSize(3)
            .taskExecutor(taskExecutor()) // 멀티쓰레드 작업을 위한
            .build();
    }

    @Bean
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

        JdbcPagingItemReader<ProductVO> reader = new JdbcPagingItemReader<>();

        reader.setDataSource(dataSource);
        reader.setPageSize(chunkSize);
        reader.setRowMapper(new BeanPropertyRowMapper(ProductVO.class));

        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause("id, name, price, type");
        queryProvider.setFromClause("from product");
        queryProvider.setWhereClause("where type = :type");

        Map<String, Order> sortKeys = new HashMap<>(1);
        sortKeys.put("id", Order.DESCENDING);
        queryProvider.setSortKeys(sortKeys);

        reader.setParameterValues(QueryGenerator.getParameterForQuery("type", productVO.getType()));
        reader.setQueryProvider(queryProvider);
        reader.afterPropertiesSet();

        return reader;
    }

    
    private TaskExecutor taskExecutor() {
        return null;
    }

    private ItemWriter<? super ProductVO> itemWriter() {
        return null;
    }

    private ItemProcessor<? super ProductVO, ? extends ProductVO> itemProcessor() {
        return null;
    }
    
}
