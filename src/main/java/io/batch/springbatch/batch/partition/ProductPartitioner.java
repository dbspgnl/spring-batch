package io.batch.springbatch.batch.partition;

import java.util.HashMap;
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
    public Map<String, ExecutionContext> partition(int gridSize) { // 실제로 쿼리문의 결과를 받아서 처리함
        ProductVO[] productList = QueryGenerator.getProductList(dataSource);
        Map<String, ExecutionContext> result = new HashMap<>();

        int number = 0;
        
        for(int i=0; i<productList.length; i++){
            ExecutionContext value = new ExecutionContext();

            result.put("partitions" + number, value);
            value.put("product", productList[i]);

            number ++;
        }

        return result;
    }
    
}
