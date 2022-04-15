package io.batch.springbatch.batch.classifier;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.item.ItemWriter;
import org.springframework.classify.Classifier;

import io.batch.springbatch.batch.model.dto.ApiRequestVO;

public class WriterClassifier<C, T> implements Classifier<C, T>{

    private Map<String, ItemWriter<ApiRequestVO>> writerMap = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public T classify(C classfiable) {
        return (T) writerMap.get(((ApiRequestVO)classfiable).getProductVO().getType());
    }

    public void setWriterMap(Map<String, ItemWriter<ApiRequestVO>> writerMap) {
        this.writerMap = writerMap;
    }
    
}
