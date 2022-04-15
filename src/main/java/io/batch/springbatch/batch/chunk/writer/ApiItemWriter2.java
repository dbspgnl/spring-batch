package io.batch.springbatch.batch.chunk.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import io.batch.springbatch.batch.model.dto.ApiRequestVO;
import io.batch.springbatch.batch.model.dto.ApiResponseVO;
import io.batch.springbatch.service.AbstractApiService;

public class ApiItemWriter2 implements ItemWriter<ApiRequestVO> {

    private final AbstractApiService apiService;
    
    public ApiItemWriter2(AbstractApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public void write(List<? extends ApiRequestVO> items) throws Exception {
        ApiResponseVO responseVO = apiService.service(items);
        System.out.println("responseVO = "+responseVO);
    }
    
}
