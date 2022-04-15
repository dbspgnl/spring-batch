package io.batch.springbatch.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import io.batch.springbatch.batch.model.domain.ApiInfo;
import io.batch.springbatch.batch.model.dto.ApiResponseVO;

@Service
public class ApiService3 extends AbstractApiService{

    @Override
    protected ApiResponseVO doApiService(RestTemplate restTemplate, ApiInfo apiInfo) {
        
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:8083/api/product/3", apiInfo, String.class);
        int statusCodeValue = responseEntity.getStatusCodeValue();
        ApiResponseVO apiResponseVO = ApiResponseVO.builder().status(statusCodeValue).msg(responseEntity.getBody()).build();
        
        return apiResponseVO;
    }
    
}
