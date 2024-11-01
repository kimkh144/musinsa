package com.musinsa.domain.product.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.musinsa.domain.product.dto.BrandCategoryDto;
import com.musinsa.domain.product.dto.BrandInfoDto;
import com.musinsa.domain.product.dto.BrandLowestPriceDto;
import com.musinsa.domain.product.dto.BrandLowestPriceResultDto;
import com.musinsa.domain.product.dto.BrandPriceLowestAPI2Dto;
import com.musinsa.domain.product.dto.BrandPriceLowestAPI3Dto;
import com.musinsa.domain.product.dto.BrandPriceLowestAPI4Dto;
import com.musinsa.domain.product.dto.CategoryPriceLowestAndHighestDto;
import com.musinsa.domain.product.entity.BrandCategoryEntity;
import com.musinsa.domain.product.entity.BrandCategoryId;
import com.musinsa.domain.product.entity.BrandInfoEntity;
import com.musinsa.domain.product.repository.BrandInfoRepository;
import com.musinsa.domain.product.repository.BrandCategoryRepository;
import com.musinsa.global.common.exception.ServiceException;
import com.musinsa.global.utils.CategoryUtils;

import io.micrometer.common.util.StringUtils;
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
	public void createBrandV2(BrandCategoryDto brandCategoryDto) {
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

	/* 브랜드 상품 등록 */
	@Transactional
	public void createBrand(BrandInfoDto brandInfoDto) {
		/* 브랜드 유효성 조회 */
		Optional<BrandInfoEntity> brandInfoEntity = brandInfoRepository.findByBrand(brandInfoDto.getBrand());
		if (brandInfoEntity.isPresent()) {
			throw new ServiceException(HttpStatus.BAD_REQUEST.value(), "이미 존재하는 브랜드 정보 입니다.");
		}

		BrandInfoEntity createBrandInfoEntity = BrandInfoEntity.builder()
			.brandInfoDto(brandInfoDto)
			.build();

		brandInfoRepository.save(createBrandInfoEntity);
	}

	/* 브랜드 상품 수정 */
	@Transactional
	public void updateBrand(String brand, BrandInfoDto brandInfoDto) {
		/* 브랜드 유효성 조회 */
		if (!brand.equals(brandInfoDto.getBrand())) {
			throw new ServiceException(HttpStatus.BAD_REQUEST.value(), "브랜드 정보가 일치 하지 않습니다.");
		}

		BrandInfoEntity brandInfoEntity = brandInfoRepository.findByBrand(brandInfoDto.getBrand())
			.orElseThrow(() -> new ServiceException(HttpStatus.BAD_REQUEST.value(), "브랜드 정보가 존재 하지 않습니다."));

		BrandInfoEntity updateBrandInfoEntity = BrandInfoEntity.builder()
			.brandInfoDto(brandInfoDto)
			.build();

		brandInfoRepository.save(updateBrandInfoEntity);
	}

	@Transactional
	public void updateBrandV2(BrandCategoryDto brandCategoryDto) {
		/* 유효성 확인 */
		BrandCategoryEntity brandCategoryEntity = brandCategoryRepository.findById_BrandAndId_Category(
				brandCategoryDto.getBrand(), brandCategoryDto.getCategory())
			.orElseThrow(() -> new ServiceException(HttpStatus.BAD_REQUEST.value(), "브랜드, 카테고리 정보가 존재하지 않습니다."));

		BrandCategoryEntity updateBrandCategoryEntity = brandCategoryEntity.toBuilder()
			.price(brandCategoryDto.getPrice())
			.build();
		brandCategoryRepository.save(updateBrandCategoryEntity);
	}

	/* 브랜드 삭제 */
	@Transactional
	public void deleteBrand(String brand) {
		/* 브랜드 유효성 조회 */
		if (StringUtils.isEmpty(brand)) {
			throw new ServiceException(HttpStatus.BAD_REQUEST.value(), "브랜드 정보가 유효 하지 않습니다.");
		}
		BrandInfoEntity brandInfoEntity = brandInfoRepository.findByBrand(brand)
			.orElseThrow(() -> new ServiceException(HttpStatus.BAD_REQUEST.value(), "브랜드 정보가 존재 하지 않습니다."));
		brandInfoRepository.delete(brandInfoEntity);
	}

	@Transactional
	public void deleteBrandV2(BrandCategoryDto brandCategoryDto) {

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

	/* 전체 조회 */
	public BrandLowestPriceResultDto getCategoryV2() {
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

	/* 단일 브랜드 */
	public BrandPriceLowestAPI2Dto getBrand() {
		List<BrandCategoryEntity> brandCategoryEntities = brandCategoryRepository.getBrandPriceV2();
		Long totalPrice = brandCategoryEntities.stream()
			.mapToLong(BrandCategoryEntity -> BrandCategoryEntity.getPrice())
			.sum();

		List<BrandPriceLowestAPI4Dto> brandLowestPriceDtos = brandCategoryEntities.stream()
			.map(brandCategoryEntity -> new BrandPriceLowestAPI4Dto(brandCategoryEntity.getId().getCategory(), brandCategoryEntity.getPrice()))
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
