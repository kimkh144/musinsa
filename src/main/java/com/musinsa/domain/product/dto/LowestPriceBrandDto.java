package com.musinsa.domain.product.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(title = "단일 브랜드 최저가 정보")
public class LowestPriceBrandDto {
	@Schema(title = "최저가 정보", required = true)
	@JsonProperty("최저가")
	private LowestPriceBrandCategoryDto lowestPriceBrandCategoryDto;
}
