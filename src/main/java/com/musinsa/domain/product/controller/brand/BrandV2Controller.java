package com.musinsa.domain.product.controller.brand;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.musinsa.domain.product.dto.BrandCategoryDto;
import com.musinsa.domain.product.dto.BrandInfoDto;
import com.musinsa.domain.product.dto.BrandLowestPriceResultDto;
import com.musinsa.domain.product.dto.CategoryPriceLowestAndHighestDto;
import com.musinsa.domain.product.service.BrandService;
import com.musinsa.global.common.response.BasicResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Created by kimkh on 2024. 10. 29..
 */
@Tag(name = "브랜드")
@RequestMapping(value = "v2/categories")
@RequiredArgsConstructor
@RestController
public class BrandV2Controller {
    private final BrandService brandService;

    /**
     * 브랜드 목록 조회
     */
    @Operation(summary = "구현 1) - 카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회하는 API", description = "브랜드 목록 조회", responses = {
        @ApiResponse(responseCode = "200", description = "브랜드 목록 조회", content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = BrandLowestPriceResultDto.class))
        })
    })
    @GetMapping(value = "")
    public Object category() {
        return BasicResponseDTO.builder()
            .data(brandService.getCategoryV2())
            .build();
    }

    /**
     * 브랜드 목록 조회
     */
    @Operation(summary = "구현 2) - 카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회하는 API", description = "브랜드 목록 조회", responses = {
        @ApiResponse(responseCode = "200", description = "카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회하는 API", content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = BrandLowestPriceResultDto.class))
        })
    })
    @GetMapping(value = "brands")
    public Object brands() {
        return BasicResponseDTO.builder()
            .data(brandService.getBrand())
            .build();
    }

    @Operation(summary = "구현 3) - 카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회하는 API", description = "", responses = {
        @ApiResponse(responseCode = "200", description = "브랜드 카테고리 목록 조회", content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = CategoryPriceLowestAndHighestDto.class))
        })
    })

    @GetMapping(value = "{categoryName}")
    public Object getCategory(
        @Parameter(description = "카테고리 명", in = ParameterIn.PATH, example = "상의")
        @PathVariable(name = "categoryName") String categoryName
    ) {
        return BasicResponseDTO.builder()
            .data(brandService.getCategoryPriceLowestAndHighestV2(categoryName))
            .build();
    }

    /**
     * 브랜드 상품 추가
     */
    @Operation(summary = "구현 4) 브랜드 및 상품을 추가", description = "", responses = {
        @ApiResponse(responseCode = "200", description = "브랜드 및 상품 등록", content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = BrandInfoDto.class))
        })
    })

    @PostMapping(value = "")
    public ResponseEntity<BasicResponseDTO> createCategory(
        @RequestBody @Valid BrandCategoryDto brandCategoryDto
    ) {
        brandService.createBrandV2(brandCategoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(BasicResponseDTO.builder()
            .data(brandCategoryDto)
            .build());
    }

    /**
     * 브랜드 및 상품 업데이트
     */
    @Operation(summary = "구현 4) 브랜드 및 상품을 업데이트 API ", description = "", responses = {
        @ApiResponse(responseCode = "200", description = "브랜드 및 상품 수정", content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = BrandInfoDto.class))
        })
    })

    @PutMapping(value = "")
    public Object updateCategory(
        // @Parameter(description = "브랜드", in = ParameterIn.PATH, example = "A")
        @RequestBody @Valid BrandCategoryDto brandCategoryDto
    ) {
        brandService.updateBrandV2(brandCategoryDto);
        return BasicResponseDTO.builder()
            .data(brandCategoryDto)
            .build();
    }

    /**
     * 브랜드 및 상품 삭제
     */
    @Operation(summary = "구현 4) 브랜드 및 상품을 삭제 API ", description = "브랜드 상품 삭제", responses = {
        @ApiResponse(responseCode = "200", description = "브랜드 및 상품 삭제", content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = Object.class))
        })
    })

    @DeleteMapping(value = "")
    public ResponseEntity<BasicResponseDTO> deleteCategory(
        // @Parameter(description = "브랜드 명", in = ParameterIn.PATH, example = "A")
        // @PathVariable(name = "brand") String brand
        @RequestBody @Valid BrandCategoryDto brandCategoryDto
    ) {
        brandService.deleteBrandV2(brandCategoryDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
