package com.musinsa.domain.product.repository.Custom.Impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.musinsa.domain.product.dto.BrandPriceDto;
import com.musinsa.domain.product.dto.CategoryPriceLowestAndHighestDto;
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
    public List<BrandCategoryEntity> findLowestPriceProductsByBrand() {
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

    @Override
    public CategoryPriceLowestAndHighestDto findCategoryPriceLowestAndHighest(String categoryName){
        List<BrandPriceDto> minPrice = getBrandPrice(categoryName, false);
        List<BrandPriceDto> maxPrice = getBrandPrice(categoryName, true);

        return CategoryPriceLowestAndHighestDto.builder()
            .category(categoryName)
            .lowerPrice(minPrice)
            .highPrice(maxPrice)
            .build();
    }

    /* 상품 가격 정보 조회 */
    private List<BrandPriceDto> getBrandPrice(String category, boolean isMax) {
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
