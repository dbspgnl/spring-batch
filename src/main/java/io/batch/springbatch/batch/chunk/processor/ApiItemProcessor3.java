package io.batch.springbatch.batch.chunk.processor;

import org.springframework.batch.item.ItemProcessor;

import io.batch.springbatch.batch.model.dto.ApiRequestVO;
import io.batch.springbatch.batch.model.dto.ProductVO;

public class ApiItemProcessor3 implements ItemProcessor<ProductVO, ApiRequestVO>{

    @Override
    public ApiRequestVO process(ProductVO item) throws Exception {
        return ApiRequestVO.builder()
            .id(item.getId())
            .productVO(item)
            .build();
    }
    
}
