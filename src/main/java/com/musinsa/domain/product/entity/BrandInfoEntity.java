package com.musinsa.domain.product.entity;

import com.musinsa.domain.product.dto.BrandInfoDto;
import com.musinsa.domain.product.dto.CategoryInfoDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * Created by kimkh on 2024. 10. 29..
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_BRAND_INFO")
public class BrandInfoEntity {

	@Id
	@Column(name = "브랜드", nullable = false, length = 4)
	private String brand;

	@Column(name = "상의", nullable = false)
	private Long top;

	@Column(name = "아우터", nullable = false)
	private Long outer;

	@Column(name = "바지", nullable = false)
	private Long pants;

	@Column(name = "스니커즈", nullable = false)
	private Long sneakers;

	@Column(name = "가방", nullable = false)
	private Long bag;

	@Column(name = "모자", nullable = false)
	private Long hat;

	@Column(name = "양말", nullable = false)
	private Long socks;

	@Column(name = "액세서리", nullable = false)
	private Long accessories;

	@Builder
	public BrandInfoEntity(BrandInfoDto brandInfoDto) {
		this.brand = brandInfoDto.getBrand();
		for (CategoryInfoDto categoryInfoDto : brandInfoDto.getCategories()) {
			switch (categoryInfoDto.getCategory()) {
				case "상의":
					this.top = categoryInfoDto.getPrice();
					break;
				case "아우터":
					this.outer = categoryInfoDto.getPrice();
					break;
				case "바지":
					this.pants = categoryInfoDto.getPrice();
					break;
				case "스니커즈":
					this.sneakers = categoryInfoDto.getPrice();
					break;
				case "가방":
					this.bag = categoryInfoDto.getPrice();
					break;
				case "모자":
					this.hat = categoryInfoDto.getPrice();
					break;
				case "양말":
					this.socks = categoryInfoDto.getPrice();
					break;
				case "액세서리":
					this.accessories = categoryInfoDto.getPrice();
					break;
				default:
					throw new IllegalArgumentException("유효하지 않은 항목입니다: " + categoryInfoDto.getCategory());
			}
		}
	}
}
