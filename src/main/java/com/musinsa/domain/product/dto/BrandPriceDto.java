package com.musinsa.domain.product.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.musinsa.global.utils.StringFormatUtils;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by kimkh on 10/30/24.
 */

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BrandPriceDto {
	@JsonProperty("브랜드")
	private String brand;
	@JsonProperty("가격")
	private String price;

	@Builder
	public BrandPriceDto(String brand, String price) {
		this.brand = brand;
		this.price = StringFormatUtils.setPriceComma(price);
	}

	public BrandPriceDto(String brand, Long price) {
		this.brand = brand;
		this.price = StringFormatUtils.setPriceComma(price.toString());
	}

}
