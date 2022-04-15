package io.batch.springbatch.batch.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiRequestVO {
    
    private long id;
    private ProductVO productVO;
    
}
