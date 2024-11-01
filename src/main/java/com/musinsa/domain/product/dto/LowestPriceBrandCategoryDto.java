package com.musinsa.domain.product.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.musinsa.global.utils.StringFormatUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * Created by kimkh on 10/30/24.
 */

@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LowestPriceBrandCategoryDto {
	@JsonProperty("브랜드")
	private String brand;
	@JsonProperty("카테고리")
	private List<LowestPriceCategoryDto> categories;
	@JsonProperty("총액")
	private String totalPrice;

	@Builder
	public LowestPriceBrandCategoryDto(String brand, List<LowestPriceCategoryDto> categories, Long totalPrice) {
		this.brand = brand;
		this.categories = categories;
		this.totalPrice = StringFormatUtils.setPriceComma(totalPrice.toString());
	}
}
