package com.musinsa.domain.product.dto;

import java.util.List;

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
public class BrandLowestPriceResultDto {
	@Schema(title = "상품 정보")
	private List<BrandLowestPriceDto> products;
	@Schema(title = "총액", example = "1,000")
	private String totalPrice;

	public BrandLowestPriceResultDto(List<BrandLowestPriceDto> products, Long totalPrice) {
		this.products = products;
		this.totalPrice = StringFormatUtils.setPriceComma(totalPrice.toString());
	}
}
