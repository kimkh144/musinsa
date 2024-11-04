package com.musinsa.backend.domain.product.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.View;

import com.musinsa.backend.domain.product.repository.BrandCategoryRepository;
import com.musinsa.backend.global.enums.ErrorCode;
import com.musinsa.backend.global.common.exception.ServiceException;
import com.musinsa.backend.global.utils.CategoryUtils;
import com.musinsa.backend.domain.product.dto.request.RequestBrandCategoryDto;
import com.musinsa.backend.domain.product.dto.ProductPriceDto;
import com.musinsa.backend.domain.product.dto.BrandLowestPriceResultDto;
import com.musinsa.backend.domain.product.dto.LowestPriceBrandDto;
import com.musinsa.backend.domain.product.dto.LowestPriceBrandCategoryDto;
import com.musinsa.backend.domain.product.dto.LowestPriceCategoryDto;
import com.musinsa.backend.domain.product.dto.CategoryPriceLowestAndHighestDto;
import com.musinsa.backend.domain.product.entity.BrandCategoryEntity;
import com.musinsa.backend.domain.product.entity.BrandCategoryId;

import lombok.RequiredArgsConstructor;

/**
 * Created by kimkh on 2024. 10. 29..
 */
@Service
@RequiredArgsConstructor
public class BrandCategoryService {
    private final BrandCategoryRepository brandCategoryRepository;
    private final View error;

    /* 브랜드 상품 등록 */
    @Transactional
    public void createBrand(RequestBrandCategoryDto requestBrandCategoryDto) {
        /* 브랜드 유효성 조회 */
        Optional<BrandCategoryEntity> brandCategoryEntity = brandCategoryRepository.findByIdBrandAndIdCategory(
            requestBrandCategoryDto.getBrand(), requestBrandCategoryDto.getCategory());
        if (brandCategoryEntity.isPresent()) {
            throw new ServiceException(HttpStatus.BAD_REQUEST.value(), ErrorCode.VL00005, "동일한 브랜드, 카테고리 정보가 존재 합니다.");
        }

        BrandCategoryEntity createBrandCategoryEntity = BrandCategoryEntity.builder()
            .id(BrandCategoryId.builder()
                .brand(requestBrandCategoryDto.getBrand())
                .category(requestBrandCategoryDto.getCategory())
                .build())
            .price(requestBrandCategoryDto.getPrice())
            .build();

        brandCategoryRepository.save(createBrandCategoryEntity);
    }

    @Transactional
    public void updateBrand(RequestBrandCategoryDto requestBrandCategoryDto) {
        BrandCategoryEntity brandCategoryEntity = getBrandCategory(requestBrandCategoryDto);
        BrandCategoryEntity updateBrandCategoryEntity = brandCategoryEntity.toBuilder()
            .price(requestBrandCategoryDto.getPrice())
            .build();
        brandCategoryRepository.save(updateBrandCategoryEntity);
    }

	@Transactional
	public void deleteBrand(RequestBrandCategoryDto requestBrandCategoryDto) {
		BrandCategoryEntity brandCategoryEntity = getBrandCategory(requestBrandCategoryDto);
		brandCategoryRepository.delete(brandCategoryEntity);
	}

    public BrandLowestPriceResultDto getLowestPriceProductsByCategory() {
        List<BrandCategoryEntity> brandCategoryEntities = brandCategoryRepository.findLowestPriceProductsByCategory();
        Long totalPrice = brandCategoryEntities.stream()
            .mapToLong(BrandCategoryEntity::getPrice)
            .sum();

        List<ProductPriceDto> brandLowestPriceDtos = brandCategoryEntities.stream()
            .map(brandCategoryEntity -> new ProductPriceDto(brandCategoryEntity.getId().getCategory(),
                brandCategoryEntity.getId().getBrand(), brandCategoryEntity.getPrice()))
            .sorted(Comparator.comparing(dto -> CategoryUtils.VALID_CATEGORIES.indexOf(dto.getCategory())))
            .toList();

        return new BrandLowestPriceResultDto(brandLowestPriceDtos, totalPrice);
    }

    public LowestPriceBrandDto getLowestPriceProductsByBrand() {
        List<BrandCategoryEntity> brandCategoryEntities = brandCategoryRepository.findLowestPriceBrand();
        Long totalPrice = brandCategoryEntities.stream()
            .mapToLong(BrandCategoryEntity::getPrice)
            .sum();

        List<LowestPriceCategoryDto> brandLowestPriceDtos = brandCategoryEntities.stream()
            .map(brandCategoryEntity -> new LowestPriceCategoryDto(brandCategoryEntity.getId().getCategory(),
                brandCategoryEntity.getPrice()))
            .sorted(Comparator.comparing(dto -> CategoryUtils.VALID_CATEGORIES.indexOf(dto.getCategory())))
            .toList();

        return LowestPriceBrandDto.builder()
            .lowestPriceBrandCategoryDto(LowestPriceBrandCategoryDto.builder()
                .brand(brandCategoryEntities.get(0).getId().getBrand())
                .categories(brandLowestPriceDtos)
                .totalPrice(totalPrice)
                .build())
            .build();
    }

    /* 카테고리 최저가, 최고가 조회 */
    public CategoryPriceLowestAndHighestDto getCategoryPriceLowestAndHighest(String categoryName) {
        /* 유효성 검증 */
        if (!CategoryUtils.isValidCategory(categoryName)) {
            throw new ServiceException(HttpStatus.BAD_REQUEST.value(), ErrorCode.VL00005, "유효하지 않은 카테고리 명 입니다.");
        }
        return brandCategoryRepository.findCategoryPriceLowestAndHighest(categoryName);
    }

	private BrandCategoryEntity getBrandCategory(RequestBrandCategoryDto requestBrandCategoryDto) {
		return brandCategoryRepository.findByIdBrandAndIdCategory(
				requestBrandCategoryDto.getBrand(), requestBrandCategoryDto.getCategory())
            .orElseThrow(() -> new ServiceException(HttpStatus.BAD_REQUEST.value(), ErrorCode.VL00005,
                "브랜드, 카테고리 정보가 존재하지 않습니다."));
	}
}
