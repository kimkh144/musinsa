package com.musinsa.domain.product.repository.Custom.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import com.musinsa.domain.product.dto.BrandLowestPriceDto;
import com.musinsa.domain.product.dto.BrandPriceDto;
import com.musinsa.domain.product.dto.CategoryPriceLowestAndHighestDto;
import com.musinsa.domain.product.entity.BrandInfoEntity;
import com.musinsa.domain.product.entity.QBrandCategoryEntity;
import com.musinsa.domain.product.entity.QBrandInfoEntity;
import com.musinsa.domain.product.repository.Custom.BrandInfoRepositoryCustom;
import com.musinsa.global.common.exception.ServiceException;
import com.musinsa.global.utils.CategoryUtils;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

/**
 * Created by kimkh on 2024. 10. 29..
 */
@Repository
@RequiredArgsConstructor
public class BrandInfoRepositoryCustomImpl implements BrandInfoRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;
	private final QBrandInfoEntity qBrandInfoEntity = QBrandInfoEntity.brandInfoEntity;
	private final QBrandCategoryEntity qBrandCategoryEntity = QBrandCategoryEntity.brandCategoryEntity;


	@Override
	public CategoryPriceLowestAndHighestDto getCategoryV2(String categoryName){
		List<BrandPriceDto> minPrice = getBrandPriceV2(categoryName, false);
		List<BrandPriceDto> maxPrice = getBrandPriceV2(categoryName, true);

		return CategoryPriceLowestAndHighestDto.builder()
			.category(categoryName)
			.lowerPrice(minPrice)
			.highPrice(maxPrice)
			.build();
	}

	@Override
	public CategoryPriceLowestAndHighestDto getCategory(String categoryName){
		BrandPriceDto minPrice = getBrandPrice(categoryName, getCondition(categoryName), false);
		BrandPriceDto maxPrice = getBrandPrice(categoryName, getCondition(categoryName), true);

		return CategoryPriceLowestAndHighestDto.builder()
			.category(categoryName)
			.lowerPrice(List.of(minPrice))
			.highPrice(List.of(maxPrice))
			.build();
	}

	/*public List<BrandLowestPriceDto> findByBrand(String brand) {
		return jpaQueryFactory.select(Projections.constructor(BrandLowestPriceDto.class, qBrandInfoEntity.brand, qBrandInfoEntity.bag))
			.from(qBrandInfoEntity)
			.where(qBrandInfoEntity.brand.eq(brand))
			.fetch();
	}*/

	/**
	 *
	 * @param
	 * @return
	 */
	@Override
	public List<BrandLowestPriceDto> getBrandAll() {
		List<BrandLowestPriceDto> brandLowestPriceDtos = new ArrayList<>();
		CategoryUtils.VALID_CATEGORIES.stream()
			.map(item -> getMinPrice(item, getCondition(item))) // 각 항목에 대해 getMinPrice를 호출하여 변환
			.forEach(brandLowestPriceDtos::add); // 결과를 brandLowestPriceDtos에 추가
		return brandLowestPriceDtos;
	}

	private NumberExpression<Long> getCondition(String category) {

		NumberExpression<Long> priceField = switch (category) {
			case "상의" -> qBrandInfoEntity.top;
			case "아우터" -> qBrandInfoEntity.outer;
			case "바지" -> qBrandInfoEntity.pants;
			case "스니커즈" -> qBrandInfoEntity.sneakers;
			case "가방" -> qBrandInfoEntity.bag;
			case "모자" -> qBrandInfoEntity.hat;
			case "양말" -> qBrandInfoEntity.socks;
			case "액세서리" -> qBrandInfoEntity.accessories;
			default -> throw new ServiceException(HttpStatus.BAD_REQUEST.value(), "유효하지 않은 카테고리 항목 입니다 ");
		};
		return priceField;
	}

	private BrandLowestPriceDto getMinPrice(String 항목, NumberExpression<Long> priceField) {
		// '상의' 최저 가격을 조회
		Long minPrice = jpaQueryFactory
			.select(priceField.min())
			.from(qBrandInfoEntity)
			.fetchOne();

		return
			jpaQueryFactory.select(Projections.constructor(BrandLowestPriceDto.class,
					Expressions.constant(항목),
					qBrandInfoEntity.brand.as("brand"),
					priceField.as("price")
				))
				.from(qBrandInfoEntity)
				.where(priceField.eq(minPrice))
				.orderBy(priceField.asc())
				.limit(1)
				.fetchOne();
	}

	private BrandPriceDto getBrandPrice(String 항목, NumberExpression<Long> priceField, boolean isMax) {
		// 최저, 최고 가격을 조회
		Long price = jpaQueryFactory
			.select((isMax) ? priceField.max() : priceField.min())
			.from(qBrandInfoEntity)
			.fetchOne();

		return
			jpaQueryFactory.select(Projections.constructor(BrandPriceDto.class,
					qBrandInfoEntity.brand.as("brand"),
					priceField.as("price")
				))
				.from(qBrandInfoEntity)
				.where(priceField.eq(price))
				.orderBy(priceField.asc())
				.limit(1)
				.fetchOne();
	}

	private List<BrandPriceDto> getBrandPriceV2(String category, boolean isMax) {
		// 최저, 최고 가격을 조회

		Long price = jpaQueryFactory
			.select((isMax) ? qBrandCategoryEntity.price.max() : qBrandCategoryEntity.price.min())
			.from(qBrandCategoryEntity)
			.where(qBrandCategoryEntity.id.category.eq(category))
			.fetchOne();

		return
			jpaQueryFactory.select(Projections.constructor(BrandPriceDto.class,
					qBrandCategoryEntity.id.brand.as("brand"),
					qBrandCategoryEntity.price.as("price")
				))
				.from(qBrandCategoryEntity)
				.where(qBrandCategoryEntity.price.eq(price))
				.orderBy(qBrandCategoryEntity.id.category.asc())
				.fetch();
	}
}
