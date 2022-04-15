package io.batch.springbatch.batch.model.domain;

import java.util.List;

import io.batch.springbatch.batch.model.dto.ApiRequestVO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiInfo {

    private String url;
    private List<? extends ApiRequestVO> apiRequestList;
    
    
}
