package com.musinsa.domain.product.dto;

import java.util.List;

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
public class BrandLowestPriceResultDto {
	private List<BrandLowestPriceDto> brand;
	private String totalPrice;

	public BrandLowestPriceResultDto(List<BrandLowestPriceDto> brand, Long totalPrice) {
		this.brand = brand;
		this.totalPrice = StringFormatUtils.setPriceComma(totalPrice.toString());
	}
}
