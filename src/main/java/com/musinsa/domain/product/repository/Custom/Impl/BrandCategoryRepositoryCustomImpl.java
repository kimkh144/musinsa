package com.musinsa.domain.product.repository.Custom.Impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.musinsa.domain.product.dto.CategoryPriceLowestAndHighestDto;
import com.musinsa.domain.product.dto.ProductPriceDto;
import com.musinsa.domain.product.entity.BrandCategoryEntity;
import com.musinsa.domain.product.entity.QBrandCategoryEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

/**
 * Created by kimkh on 2024. 10. 29..
 */
@Repository
@RequiredArgsConstructor
public class BrandCategoryRepositoryCustomImpl implements BrandCategoryRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final QBrandCategoryEntity qBrandCategoryEntity = QBrandCategoryEntity.brandCategoryEntity;

    @Override
    public List<BrandCategoryEntity> findLowestPriceBrand() {
        String lowestPriceBrand = jpaQueryFactory
            .select(qBrandCategoryEntity.id.brand)
            .from(qBrandCategoryEntity)
            .groupBy(qBrandCategoryEntity.id.brand)
            .orderBy(qBrandCategoryEntity.price.sum().asc())
            .limit(1)
            .fetchOne();

        return
            jpaQueryFactory.select(
                    qBrandCategoryEntity
                )
                .from(qBrandCategoryEntity)
                .where(qBrandCategoryEntity.id.brand.eq(lowestPriceBrand))
                .fetch();
    }

    /*

     */
    @Override
    public CategoryPriceLowestAndHighestDto findCategoryPriceLowestAndHighest(String category){
        List<ProductPriceDto> minPrice = findBrandPriceByCategory(category, false);
        List<ProductPriceDto> maxPrice = findBrandPriceByCategory(category, true);

        return CategoryPriceLowestAndHighestDto.builder()
            .category(category)
            .lowerPrice(minPrice)
            .highPrice(maxPrice)
            .build();
    }

    /* 상품 가격 정보 조회 */
    private List<ProductPriceDto> findBrandPriceByCategory(String category, boolean isMax) {
        /* 카테고리의 상품 최저, 최대 가격 조회 */
        Long price = jpaQueryFactory
            .select((isMax) ? qBrandCategoryEntity.price.max() : qBrandCategoryEntity.price.min())
            .from(qBrandCategoryEntity)
            .where(qBrandCategoryEntity.id.category.eq(category))
            .fetchOne();

        /* 카테고리 최저, 최대 가격의 브랜드 정보 조회 */
        return
            jpaQueryFactory.select(Projections.constructor(ProductPriceDto.class,
                    qBrandCategoryEntity.id.brand.as("brand"),
                    qBrandCategoryEntity.price.as("price")
                ))
                .from(qBrandCategoryEntity)
                .where(qBrandCategoryEntity.id.category.eq(category).and(qBrandCategoryEntity.price.eq(price)))
                .orderBy(qBrandCategoryEntity.id.category.asc())
                .fetch();
    }
}
