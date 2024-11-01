package com.musinsa.domain.product.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.musinsa.domain.product.dto.BrandInfoDto;
import com.musinsa.domain.product.dto.CategoryInfoDto;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by kimkh on 2024. 10. 29..
 */
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "TB_BRAND_CATEGORY")
public class BrandCategoryEntity {

	@EmbeddedId
	private BrandCategoryId id;

    /*@Id
    @Column(name = "BRAND", length = 1)
    private String brand;

    @Id
    @Column(name = "CATEGORY", length = 20)
    private String category;*/

    @Column(name = "PRICE", nullable = false)
    @Builder.Default
    private Long price = 0L;

}
