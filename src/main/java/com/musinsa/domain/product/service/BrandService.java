package com.musinsa.domain.product.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.musinsa.domain.product.dto.BrandCategoryDto;
import com.musinsa.domain.product.dto.BrandLowestPriceDto;
import com.musinsa.domain.product.dto.BrandLowestPriceResultDto;
import com.musinsa.domain.product.dto.LowestPriceBrandDto;
import com.musinsa.domain.product.dto.LowestPriceBrandCategoryDto;
import com.musinsa.domain.product.dto.LowestPriceCategoryDto;
import com.musinsa.domain.product.dto.CategoryPriceLowestAndHighestDto;
import com.musinsa.domain.product.entity.BrandCategoryEntity;
import com.musinsa.domain.product.entity.BrandCategoryId;
import com.musinsa.domain.product.repository.BrandCategoryRepository;
import com.musinsa.domain.product.repository.BrandInfoRepository;
import com.musinsa.global.common.exception.ServiceException;
import com.musinsa.global.utils.CategoryUtils;

import lombok.RequiredArgsConstructor;

/**
 * Created by kimkh on 2024. 10. 29..
 */
@Service
@RequiredArgsConstructor
public class BrandService {
    private final BrandInfoRepository brandInfoRepository;
    private final BrandCategoryRepository brandCategoryRepository;

    /* 브랜드 상품 등록 */
    @Transactional
    public void createBrand(BrandCategoryDto brandCategoryDto) {
        /* 브랜드 유효성 조회 */
        Optional<BrandCategoryEntity> brandCategoryEntity = brandCategoryRepository.findById_BrandAndId_Category(
            brandCategoryDto.getBrand(), brandCategoryDto.getCategory());
        if (brandCategoryEntity.isPresent()) {
            throw new ServiceException(HttpStatus.BAD_REQUEST.value(), "동일 브랜드, 카테고리 정보가 존재 합니다.");
        }

        BrandCategoryEntity createBrandCategoryEntity = BrandCategoryEntity.builder()
            .id(BrandCategoryId.builder()
                .brand(brandCategoryDto.getBrand())
                .category(brandCategoryDto.getCategory())
                .build())
            .price(brandCategoryDto.getPrice())
            .build();

        brandCategoryRepository.save(createBrandCategoryEntity);
    }

    @Transactional
    public void updateBrand(BrandCategoryDto brandCategoryDto) {
        /* 유효성 확인 */
        BrandCategoryEntity brandCategoryEntity = brandCategoryRepository.findById_BrandAndId_Category(
                brandCategoryDto.getBrand(), brandCategoryDto.getCategory())
            .orElseThrow(() -> new ServiceException(HttpStatus.BAD_REQUEST.value(), "브랜드, 카테고리 정보가 존재하지 않습니다."));

        BrandCategoryEntity updateBrandCategoryEntity = brandCategoryEntity.toBuilder()
            .price(brandCategoryDto.getPrice())
            .build();
        brandCategoryRepository.save(updateBrandCategoryEntity);
    }

    @Transactional
    public void deleteBrand(BrandCategoryDto brandCategoryDto) {

        BrandCategoryEntity brandCategoryEntity = brandCategoryRepository.findById_BrandAndId_Category(
                brandCategoryDto.getBrand(), brandCategoryDto.getCategory())
            .orElseThrow(() -> new ServiceException(HttpStatus.BAD_REQUEST.value(), "브랜드, 카테고리 정보가 존재 하지 않습니다."));

        brandCategoryRepository.delete(brandCategoryEntity);
    }

    public BrandLowestPriceResultDto getLowestPriceProductsByCategory() {
        List<BrandCategoryEntity> brandCategoryEntities = brandCategoryRepository.getBrandAllV2();
        Long totalPrice = brandCategoryEntities.stream()
            .mapToLong(BrandCategoryEntity::getPrice)
            .sum();

        List<BrandLowestPriceDto> brandLowestPriceDtos = brandCategoryEntities.stream()
            .map(brandCategoryEntity -> new BrandLowestPriceDto(brandCategoryEntity.getId().getCategory(),
                brandCategoryEntity.getId().getBrand(), brandCategoryEntity.getPrice()))
            .sorted(Comparator.comparing(dto -> CategoryUtils.VALID_CATEGORIES.indexOf(dto.getCategory())))
            .collect(Collectors.toList());

        return new BrandLowestPriceResultDto(brandLowestPriceDtos, totalPrice);
    }

    public LowestPriceBrandDto getLowestPriceProductsByBrand() {
        List<BrandCategoryEntity> brandCategoryEntities = brandCategoryRepository.getBrandPriceV2();
        Long totalPrice = brandCategoryEntities.stream()
            .mapToLong(BrandCategoryEntity::getPrice)
            .sum();

        List<LowestPriceCategoryDto> brandLowestPriceDtos = brandCategoryEntities.stream()
            .map(brandCategoryEntity -> new LowestPriceCategoryDto(brandCategoryEntity.getId().getCategory(),
                brandCategoryEntity.getPrice()))
            .sorted(Comparator.comparing(dto -> CategoryUtils.VALID_CATEGORIES.indexOf(dto.getCategory())))
            .collect(Collectors.toList());

        return LowestPriceBrandDto.builder()
            .lowestPriceBrandCategoryDto(LowestPriceBrandCategoryDto.builder()
                .brand(brandCategoryEntities.get(0).getId().getBrand())
                .categories(brandLowestPriceDtos)
                .totalPrice(totalPrice)
                .build())
            .build();
    }

    /* 카테고리 최저가, 최고가 조회 */
    public CategoryPriceLowestAndHighestDto getCategoryPriceLowestAndHighestV2(String categoryName) {
        /* 유효성 검증 */
        if (!CategoryUtils.isValidCategory(categoryName)) {
            throw new ServiceException(HttpStatus.BAD_REQUEST.value(), "유효하지 않은 카테고리 명 입니다.");
        }
        return brandInfoRepository.getCategoryV2(categoryName);
    }
}
