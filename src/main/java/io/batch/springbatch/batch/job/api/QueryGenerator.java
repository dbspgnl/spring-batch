package io.batch.springbatch.batch.job.api;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import io.batch.springbatch.batch.model.dto.ProductVO;
import io.batch.springbatch.batch.rowmapper.ProductRowMapper;

public class QueryGenerator {

    public static ProductVO[] getProductList(DataSource dataSource){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<ProductVO> productList = jdbcTemplate.query("select type from product group by type", new ProductRowMapper(){
            @Override
            public ProductVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                return ProductVO.builder().type("type").build();
            }
        });
        return productList.toArray(new ProductVO[]{}); // List<ProductVO> -> ProductVO[]
    }

    public static Map<String, Object> getParameterForQuery(String parameter, String value) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put(parameter, value);
        return parameters;
    }

}
