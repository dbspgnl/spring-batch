package io.batch.springbatch.batch.partition;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import io.batch.springbatch.batch.job.api.QueryGenerator;
import io.batch.springbatch.batch.model.dto.ProductVO;

public class ProductPartitioner implements Partitioner{

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        // ProductVO[] productList = QueryGenerator.getProductList(dataSource);
        return null;
    }
    
}
