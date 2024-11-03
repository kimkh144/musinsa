package com.musinsa.domain.product.repository.Impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.musinsa.domain.product.dto.CategoryPriceLowestAndHighestDto;
import com.musinsa.domain.product.entity.BrandCategoryEntity;

/**
 * Created by kimkh on 2024. 10. 29..
 */
@Repository
public interface BrandCategoryRepositoryCustom {

	List<BrandCategoryEntity> findLowestPriceBrand();
	CategoryPriceLowestAndHighestDto findCategoryPriceLowestAndHighest(String categoryName);
}
