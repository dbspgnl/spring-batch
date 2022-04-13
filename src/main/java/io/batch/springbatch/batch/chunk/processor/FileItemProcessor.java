package io.batch.springbatch.batch.chunk.processor;

import org.modelmapper.ModelMapper;
import org.springframework.batch.item.ItemProcessor;

import io.batch.springbatch.batch.model.domain.Product;
import io.batch.springbatch.batch.model.dto.ProductVO;

public class FileItemProcessor implements ItemProcessor<ProductVO, Product>{

    @Override
    public Product process(ProductVO item) throws Exception {
        
        ModelMapper modelMapper = new ModelMapper();
        Product product = modelMapper.map(item, Product.class);
        
        return product;
    }
    
}
