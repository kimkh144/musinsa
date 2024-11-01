package com.musinsa.domain.product.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.musinsa.domain.product.entity.BrandCategoryEntity;
import com.musinsa.domain.product.entity.BrandCategoryId;
import com.musinsa.domain.product.repository.Custom.BrandInfoRepositoryCustom;
import com.musinsa.domain.product.repository.Custom.Impl.BrandCategoryRepositoryCustom;

import jakarta.validation.constraints.NotBlank;

/**
 * Created by kimkh on 2024. 10. 29..
 */
@Repository
public interface BrandCategoryRepository extends JpaRepository<BrandCategoryEntity, BrandCategoryId>,
	BrandCategoryRepositoryCustom {

	/*@Query(value = "SELECT "
		+ "    T.CATEGORY, "
		+ "    T.BRAND, "
		+ "    T.PRICE AS MIN_PRICE "
		+ "FROM "
		+ "    TB_BRAND_CATEGORY T "
		+ "JOIN "
		+ "    (SELECT "
		+ "        CATEGORY, "
		+ "        MIN(PRICE) AS MIN_PRICE "
		+ "     FROM "
		+ "        TB_BRAND_CATEGORY "
		+ "     GROUP BY "
		+ "        CATEGORY ORDER BY CATEGORY DESC ) AS MIN_PRICE_TABLE "
		+ "ON "
		+ "    T.CATEGORY = MIN_PRICE_TABLE.CATEGORY "
		+ "    AND T.PRICE = MIN_PRICE_TABLE.MIN_PRICE", nativeQuery = true)
*/
	@Query(value = "SELECT CATEGORY, BRAND, PRICE "
		+ "    FROM ( "
		+ "    SELECT "
		+ "    T.CATEGORY, "
		+ "    T.BRAND, "
		+ "    T.PRICE, "
		+ "    ROW_NUMBER() OVER (PARTITION BY T.CATEGORY ORDER BY T.PRICE ASC, T.BRAND ASC) AS ROW_NUM "
		+ "    FROM "
		+ "    TB_BRAND_CATEGORY T "
		+ "    ) AS RANKED_TABLE "
		+ "    WHERE ROW_NUM = 1 "
		+ "    ORDER BY  CATEGORY"
		/*+ "	CASE CATEGORY"
		+ "        WHEN '상의' THEN 1"
		+ "        WHEN '아우터' THEN 2"
		+ "        WHEN '바지' THEN 3"
		+ "        WHEN '스니커즈' THEN 4"
		+ "        WHEN '가방' THEN 5"
		+ "        WHEN '모자' THEN 6"
		+ "        WHEN '양말' THEN 7"
		+ "        WHEN '액세서리' THEN 8"
		+ "        ELSE 9  -- 정의되지 않은 경우를 대비해 기타 항목의 순서도 설정\n"*//*
		+ "    END;"*/
		, nativeQuery = true)
	List<BrandCategoryEntity> getBrandAllV2();

	Optional<BrandCategoryEntity> findById_BrandAndId_Category(@NotBlank(message = "브랜드 정보는 필수 입력 되어야 합니다.") String brand, @NotBlank(message = "카테고리 정보는 필수 입력 되어야 합니다.") String category);
}
