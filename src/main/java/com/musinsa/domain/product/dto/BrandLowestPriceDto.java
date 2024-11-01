package com.musinsa.domain.product.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.musinsa.global.utils.StringFormatUtils;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by kimkh on 2024. 10. 29..
 */
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BrandLowestPriceDto {
	@Schema(title = "카테고리", example = "상의")
	private String category;
	@Schema(title = "브랜드", example = "A")
	private String brand;
	@Schema(title = "가격", example = "1,000")
	private String price;

	public BrandLowestPriceDto(String category, String brand, Long price) {
		this.category = category;
		this.brand = brand;
		this.price = StringFormatUtils.setPriceComma(price.toString());
	}
}
