package com.musinsa.domain.product.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.musinsa.domain.product.entity.BrandCategoryEntity;
import com.musinsa.domain.product.entity.BrandCategoryId;
import com.musinsa.domain.product.repository.Custom.Impl.BrandCategoryRepositoryCustom;

import jakarta.validation.constraints.NotBlank;

/**
 * Created by kimkh on 2024. 10. 29..
 */
@Repository
public interface BrandCategoryRepository extends JpaRepository<BrandCategoryEntity, BrandCategoryId>,
	BrandCategoryRepositoryCustom {

	@Query(value = """
    SELECT CATEGORY, BRAND, PRICE
    FROM (
        SELECT
            T.CATEGORY,
            T.BRAND,
            T.PRICE,
            ROW_NUMBER() OVER (PARTITION BY T.CATEGORY ORDER BY T.PRICE ASC, T.BRAND ASC) AS ROW_NUM
        FROM
            TB_BRAND_CATEGORY T
    ) AS RANKED_TABLE
    WHERE ROW_NUM = 1
    ORDER BY CATEGORY
    """, nativeQuery = true)
	List<BrandCategoryEntity> findLowestPriceProductsByCategory();

	Optional<BrandCategoryEntity> findByIdBrandAndIdCategory(
		@NotBlank(message = "브랜드 정보는 필수 입력 되어야 합니다.") String brand,
		@NotBlank(message = "카테고리 정보는 필수 입력 되어야 합니다.") String category);
}
