package com.musinsa.domain.product.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.musinsa.global.utils.StringFormatUtils;
import com.querydsl.core.util.StringUtils;

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
	private String category;
	private String brand;
	private String price;

	public BrandLowestPriceDto(String category, String brand, Long price) {
		this.category = category;
		this.brand = brand;
		this.price = StringFormatUtils.setPriceComma(price.toString());
	}
}
