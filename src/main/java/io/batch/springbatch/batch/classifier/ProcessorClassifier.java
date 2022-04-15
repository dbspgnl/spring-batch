package io.batch.springbatch.batch.classifier;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.classify.Classifier;

import io.batch.springbatch.batch.model.dto.ApiRequestVO;
import io.batch.springbatch.batch.model.dto.ProductVO;

public class ProcessorClassifier<C, T> implements Classifier<C, T>{

    private Map<String, ItemProcessor<ProductVO, ApiRequestVO>> processorMap = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public T classify(C classfiable) {
        return (T) processorMap.get(((ProductVO)classfiable).getType());
    }

    public void setProcessorMap(Map<String, ItemProcessor<ProductVO, ApiRequestVO>> processorMap) {
        this.processorMap = processorMap;
    }
    
}
