package com.asitc.pgmicroservice.repository.company;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.asitc.pgmicroservice.repository.company.model.Company;

public interface CompanyRepository extends PagingAndSortingRepository<Company, Long> {

    List<Company> findByName(@Param("name") String name);
    
}
