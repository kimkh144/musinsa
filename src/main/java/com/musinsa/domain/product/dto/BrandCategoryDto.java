package com.musinsa.domain.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by kimkh on 2024. 10. 29..
 */
@Getter
@Setter
@NoArgsConstructor
public class BrandCategoryDto {
	@NotBlank(message = "브랜드 정보는 필수 입력 되어야 합니다.")
	private String brand;
	@NotBlank(message = "카테고리 정보는 필수 입력 되어야 합니다.")
	private String category;
	@Positive(message = "가격은 0원 보다 커야 합니다.")
	private Long price;
}
