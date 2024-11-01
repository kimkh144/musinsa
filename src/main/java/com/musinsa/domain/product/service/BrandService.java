package com.musinsa.domain.product.service;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.musinsa.domain.product.dto.BrandCategoryDto;
import com.musinsa.domain.product.dto.BrandLowestPriceDto;
import com.musinsa.domain.product.dto.BrandLowestPriceResultDto;
import com.musinsa.domain.product.dto.BrandPriceLowestAPI2Dto;
import com.musinsa.domain.product.dto.BrandPriceLowestAPI3Dto;
import com.musinsa.domain.product.dto.BrandPriceLowestAPI4Dto;
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
            throw new ServiceException(HttpStatus.BAD_REQUEST.value(), "동일한 브랜드, 카테고리 정보가 존재 합니다.");
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

    /* 카테고리 최저가, 최고가 조회 */
    public CategoryPriceLowestAndHighestDto getCategoryPriceLowestAndHighest(String categoryName) {
        /* 유효성 검증 */
        if (!CategoryUtils.isValidCategory(categoryName)) {
            throw new ServiceException(HttpStatus.BAD_REQUEST.value(), "유효하지 않은 카테고리 명 입니다.");
        }
        return brandInfoRepository.getCategory(categoryName);
    }

    /* 전체 조회 */
    public BrandLowestPriceResultDto getBrandInfo() {
        List<BrandLowestPriceDto> brandLowestPriceDtos = brandInfoRepository.getBrandAll();
        Long totalPrice = brandLowestPriceDtos.stream()
            .mapToLong(dto -> Long.parseLong(dto.getPrice().replaceAll(",", "")))
            .sum();

        return new BrandLowestPriceResultDto(brandLowestPriceDtos, totalPrice);
    }

    /**
     * 카테고리 별 최저 가격 브랜드와 상품 가격, 총액을 조회하는 메소드.
     * 모든 브랜드 카테고리 데이터를 조회하여,
     * 각 카테고리에서 최저 가격을 가진 상품을 찾고,
     * 카테고리별 총 가격을 계산합니다.
     * @return BrandLowestPriceResultDto - 카테고리별 최저 가격 정보 및 총액을 담고 있는 DTO 객체.
     *         이 객체는 각 카테고리의 최저 가격 브랜드 목록과
     *         해당 카테고리의 모든 상품 가격의 총합을 포함합니다.

     * @throws NoSuchElementException - 브랜드 카테고리 데이터가 비어있는 경우, 최저 가격을 찾을 수 없어 발생할 수 있습니다.
     * @throws Exception - 데이터베이스 접근 오류 등 예외 상황이 발생할 수 있습니다.
     */
    public BrandLowestPriceResultDto getLowestPriceProductsByCategory() {
        List<BrandCategoryEntity> brandCategoryEntities = brandCategoryRepository.getBrandAllV2();
        Long totalPrice = brandCategoryEntities.stream()
            .mapToLong(BrandCategoryEntity -> BrandCategoryEntity.getPrice())
            .sum();

        List<BrandLowestPriceDto> brandLowestPriceDtos = brandCategoryEntities.stream()
            .map(brandCategoryEntity -> new BrandLowestPriceDto(brandCategoryEntity.getId().getCategory(),
                brandCategoryEntity.getId().getBrand(), brandCategoryEntity.getPrice()))
            .sorted(Comparator.comparing(dto -> CategoryUtils.VALID_CATEGORIES.indexOf(dto.getCategory())))
            .collect(Collectors.toList());

        return new BrandLowestPriceResultDto(brandLowestPriceDtos, totalPrice);
    }

    /**
     * 단일 브랜드로 모든 카테고리 상품을 구매할 때 최저 가격에 판매하는
     * 브랜드와 각 카테고리의 상품 가격, 총액을 조회하는 메소드.
     * 이 메소드는 브랜드 카테고리 데이터를 조회하여,
     * 지정된 브랜드의 모든 카테고리 상품 중 최저 가격을 가진 상품을 찾고,
     * 카테고리별 총 가격을 계산합니다.
     *
     * @return BrandPriceLowestAPI2Dto - 최저 가격 정보를 포함하는 DTO 객체.
     *         이 객체는 브랜드 이름, 각 카테고리의 최저 가격 상품 목록,
     *         그리고 총액을 포함합니다.
     *
     * @throws Exception - 데이터베이스 핸들링 오류 등 예외 사항 발생
     */
    public BrandPriceLowestAPI2Dto getLowestPriceProductsByBrand() {
        List<BrandCategoryEntity> brandCategoryEntities = brandCategoryRepository.getBrandPriceV2();
        Long totalPrice = brandCategoryEntities.stream()
            .mapToLong(BrandCategoryEntity -> BrandCategoryEntity.getPrice())
            .sum();

        List<BrandPriceLowestAPI4Dto> brandLowestPriceDtos = brandCategoryEntities.stream()
            .map(brandCategoryEntity -> new BrandPriceLowestAPI4Dto(brandCategoryEntity.getId().getCategory(),
                brandCategoryEntity.getPrice()))
            .sorted(Comparator.comparing(dto -> CategoryUtils.VALID_CATEGORIES.indexOf(dto.getCategory())))
            .collect(Collectors.toList());

        ;
        BrandPriceLowestAPI2Dto brandPriceLowestAPI2Dto = BrandPriceLowestAPI2Dto.builder()
            .lowestPrice(BrandPriceLowestAPI3Dto.builder()
                .brand(brandCategoryEntities.get(0).getId().getBrand())
                .categories(brandLowestPriceDtos)
                .totalPrice(totalPrice)
                .build())
            .build();

        return brandPriceLowestAPI2Dto;
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
