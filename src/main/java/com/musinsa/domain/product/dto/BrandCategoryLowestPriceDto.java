package com.musinsa.domain.product.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.musinsa.global.utils.StringFormatUtils;

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
public class BrandCategoryLowestPriceDto {
	private String category;
	private String brand;
	private String price;

	public BrandCategoryLowestPriceDto(String category, String brand, Long price) {
		this.category = category;
		this.brand = brand;
		this.price = StringFormatUtils.setPriceComma(price.toString());
	}
}
