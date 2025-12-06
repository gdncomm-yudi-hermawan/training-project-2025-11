package com.marketplace.seeder.repository;

import com.marketplace.seeder.document.ProductSearchDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductSearchRepository extends ElasticsearchRepository<ProductSearchDoc, String> {
}
