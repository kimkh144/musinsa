package com.musinsa.domain.product.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * Created by kimkh on 10/30/24.
 */

@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class LowestPriceBrandDto {
	@JsonProperty("최저가")
	private LowestPriceBrandCategoryDto lowestPriceBrandCategoryDto;
}
