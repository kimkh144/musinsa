package com.musinsa.domain.product.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * Created by kimkh on 10/30/24.
 */

@Getter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryInfoDto {
	@NotBlank(message = "카테고리 이름은 필수 입니다.")
	private String category;

	@NotNull(message = "카테고리 금액은 필수 입니다.")
	@Positive(message = "가격은 0원 이상 입력 되어야 합니다.")
	private Long price;
}
