package com.musinsa.domain.product.repository.Custom;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.musinsa.domain.product.dto.BrandLowestPriceDto;
import com.musinsa.domain.product.dto.CategoryPriceLowestAndHighestDto;

/**
 * Created by kimkh on 2024. 10. 29..
 */
@Repository
public interface BrandInfoRepositoryCustom {

	List<BrandLowestPriceDto> getBrandAll();
	CategoryPriceLowestAndHighestDto getCategory(String categoryName);
	CategoryPriceLowestAndHighestDto getCategoryV2(String categoryName);
}
