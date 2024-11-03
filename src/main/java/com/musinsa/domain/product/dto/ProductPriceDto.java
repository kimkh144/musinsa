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
@Schema(title = "상품 정보")
public class ProductPriceDto {
	@Schema(title = "브랜드", example = "A")
	private String brand;
	@Schema(title = "카테고리", example = "상의")
	private String category;
	@Schema(title = "상품 가격", example = "1,000")
	private String price;

	public ProductPriceDto(String category, String brand, Long price) {
		this.category = category;
		this.brand = brand;
		this.price = StringFormatUtils.setPriceComma(price.toString());
	}
}
