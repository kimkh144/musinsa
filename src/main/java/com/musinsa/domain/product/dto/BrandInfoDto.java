package com.musinsa.domain.product.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by kimkh on 2024. 10. 29..
 */
@Getter
@Setter
@NoArgsConstructor
public class BrandInfoDto {
	@Schema(title = "브랜드", required = true, example = "A")
	@NotBlank(message = "브랜드 정보는 필수 입니다.")
	private String brand;
	@Schema(title = "카테고리", required = true, example = "A")
	@NotEmpty(message = "카테고리 정보는 필수 입니다.")
	@Size(min = 1, max = 8)
	private List<@Valid CategoryInfoDto> categories;
}
