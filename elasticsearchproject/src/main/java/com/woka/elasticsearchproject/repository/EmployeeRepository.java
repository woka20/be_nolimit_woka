package com.woka.elasticsearchproject.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import com.woka.elasticsearchproject.entity.EmployeeEntity;



public interface EmployeeRepository extends ElasticsearchRepository<EmployeeEntity, String>, CustomEmployeeRepository {

}
