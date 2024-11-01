package com.musinsa.domain.product.controller.product;

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
import com.musinsa.global.constants.swagger.SwaggerConstants;

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
@Tag(name = SwaggerConstants.category.CATEGORY_TAG)
@RequestMapping(value = SwaggerConstants.category.CATEGORY_V1)
@RequiredArgsConstructor
@RestController
public class CategoryController {
    private final BrandService brandService;

    /**
     * 구현 1)- 카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회하는 API
     */
    @Operation(summary = SwaggerConstants.category.CATEGORY_API_V1_DESC, description =
        SwaggerConstants.category.CATEGORY_API_V1_VERSION
            + SwaggerConstants.category.CATEGORY_API_V1_DESC, responses = {
        @ApiResponse(responseCode = SwaggerConstants.HTTP_STATUS_OK, description = SwaggerConstants.category.CATEGORY_API_V1_DESC, content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = BrandLowestPriceResultDto.class))
        })
    })
    @GetMapping(value = SwaggerConstants.category.CATEGORY_API_V1_URL)
    public Object category() {
        return BasicResponseDTO.builder()
            .data(brandService.getLowestPriceProductsByCategory())
            .build();
    }

    /**
     * 구현 3) - 카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회하는 API
     */
    @Operation(summary = SwaggerConstants.category.CATEGORY_API_V3_DESC, description = SwaggerConstants.category.CATEGORY_API_V3_VERSION
        + SwaggerConstants.category.CATEGORY_API_V3_DESC, responses = {
        @ApiResponse(responseCode = SwaggerConstants.HTTP_STATUS_OK, description = SwaggerConstants.category.CATEGORY_API_V3_VERSION
            + SwaggerConstants.category.CATEGORY_API_V3_DESC, content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = CategoryPriceLowestAndHighestDto.class))
        })
    })

    @GetMapping(value = SwaggerConstants.category.CATEGORY_API_V3_URL)
    public Object getCategory(
        @Parameter(description = "카테고리", in = ParameterIn.PATH, example = "상의")
        @PathVariable(name = "category") String category
    ) {
        return BasicResponseDTO.builder()
            .data(brandService.getCategoryPriceLowestAndHighestV2(category))
            .build();
    }

    /**
     * 구현 4) 브랜드 및 상품을 추가 API
     */
    @Operation(summary = SwaggerConstants.brand.BRAND_API_V4_CREATE_DESC, description =
        SwaggerConstants.brand.BRAND_API_V4_VERSION + SwaggerConstants.brand.BRAND_API_V4_CREATE_DESC, responses = {
        @ApiResponse(responseCode = SwaggerConstants.HTTP_STATUS_OK, description =
            SwaggerConstants.brand.BRAND_API_V4_VERSION + SwaggerConstants.brand.BRAND_API_V4_CREATE_DESC, content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = BrandInfoDto.class))
        })
    })

    @PostMapping(value =  SwaggerConstants.brand.BRAND_API_V4_URL)
    public ResponseEntity<BasicResponseDTO> createCategory(
        @RequestBody @Valid BrandCategoryDto brandCategoryDto
    ) {
        brandService.createBrand(brandCategoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(BasicResponseDTO.builder()
            .data(brandCategoryDto)
            .build());
    }

    /**
     * 구현 4) 브랜드 및 상품을 업데이트 API
     */
    @Operation(summary = SwaggerConstants.brand.BRAND_API_V4_UPDATE_DESC, description =
        SwaggerConstants.brand.BRAND_API_V4_VERSION + SwaggerConstants.brand.BRAND_API_V4_CREATE_DESC, responses = {
        @ApiResponse(responseCode = SwaggerConstants.HTTP_STATUS_OK, description =
            SwaggerConstants.brand.BRAND_API_V4_VERSION + SwaggerConstants.brand.BRAND_API_V4_CREATE_DESC, content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = BrandInfoDto.class))
        })
    })

    @PutMapping(value = SwaggerConstants.brand.BRAND_API_V4_URL)
    public Object updateCategory(
        @RequestBody @Valid BrandCategoryDto brandCategoryDto
    ) {
        brandService.updateBrand(brandCategoryDto);
        return BasicResponseDTO.builder()
            .data(brandCategoryDto)
            .build();
    }

    /**
     * 구현 4) 브랜드 및 상품을 삭제 API
     */
    @Operation(summary = SwaggerConstants.brand.BRAND_API_V4_DELETE_DESC, description =
        SwaggerConstants.brand.BRAND_API_V4_VERSION + SwaggerConstants.brand.BRAND_API_V4_CREATE_DESC, responses = {
        @ApiResponse(responseCode = SwaggerConstants.HTTP_STATUS_OK, description =
            SwaggerConstants.brand.BRAND_API_V4_VERSION + SwaggerConstants.brand.BRAND_API_V4_DELETE_DESC, content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = Object.class))
        })
    })

    @DeleteMapping(value = SwaggerConstants.brand.BRAND_API_V4_URL)
    public ResponseEntity<BasicResponseDTO> deleteCategory(
        @RequestBody @Valid BrandCategoryDto brandCategoryDto
    ) {
        brandService.deleteBrand(brandCategoryDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
