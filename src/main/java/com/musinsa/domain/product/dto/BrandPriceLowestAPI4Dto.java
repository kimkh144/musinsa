package com.musinsa.domain.product.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.musinsa.global.utils.StringFormatUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * Created by kimkh on 10/30/24.
 */

@Getter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BrandPriceLowestAPI4Dto {
	@JsonProperty("카테고리")
	private String category;
	@JsonProperty("가격")
	private String price;

	@Builder
	public BrandPriceLowestAPI4Dto(String category, Long price) {
		this.category = category;
		this.price = StringFormatUtils.setPriceComma(price.toString());
	}

}
