package io.batch.springbatch.batch.chunk.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import io.batch.springbatch.batch.model.dto.ApiRequestVO;

public class ApiItemWriter1 implements ItemWriter<ApiRequestVO> {

    @Override
    public void write(List<? extends ApiRequestVO> arg0) throws Exception {
        
    }
    
}
