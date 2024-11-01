package com.musinsa.domain.product.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * Created by kimkh on 10/30/24.
 */

@Getter
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryPriceLowestAndHighestDto {
	@JsonProperty("카테고리")
	private String category;
	@JsonProperty("최저가")
	private List<BrandPriceDto> lowerPrice;
	@JsonProperty("최고가")
	private List<BrandPriceDto> highPrice;
}
