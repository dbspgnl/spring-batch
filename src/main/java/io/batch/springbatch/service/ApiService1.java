package io.batch.springbatch.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import io.batch.springbatch.batch.model.domain.ApiInfo;
import io.batch.springbatch.batch.model.dto.ApiResponseVO;

public class ApiService1 extends AbstractApiService{

    @Override
    protected ApiResponseVO doApiService(RestTemplate restTemplate, ApiInfo apiInfo) {
        
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:8081/api/product/1", apiInfo, String.class);
        int statusCodeValue = responseEntity.getStatusCodeValue();
        ApiResponseVO apiResponseVO = ApiResponseVO.builder().status(statusCodeValue).msg(responseEntity.getBody()).build();
        
        return apiResponseVO;
    }
    
}
