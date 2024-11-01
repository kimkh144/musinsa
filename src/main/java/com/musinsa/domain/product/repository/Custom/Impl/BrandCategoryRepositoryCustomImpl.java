package com.musinsa.domain.product.repository.Custom.Impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import com.musinsa.domain.product.dto.BrandLowestPriceDto;
import com.musinsa.domain.product.dto.BrandPriceDto;
import com.musinsa.domain.product.dto.CategoryPriceLowestAndHighestDto;
import com.musinsa.domain.product.entity.BrandCategoryEntity;
import com.musinsa.domain.product.entity.BrandCategoryId;
import com.musinsa.domain.product.entity.QBrandCategoryEntity;
import com.musinsa.domain.product.entity.QBrandInfoEntity;
import com.musinsa.domain.product.repository.BrandCategoryRepository;
import com.musinsa.domain.product.repository.Custom.BrandInfoRepositoryCustom;
import com.musinsa.global.common.exception.ServiceException;
import com.musinsa.global.utils.CategoryUtils;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
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
    public List<BrandCategoryEntity> getBrandPriceV2() {
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
}
