package com.musinsa.domain.product.controller.product;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.musinsa.domain.product.dto.LowestPriceBrandDto;
import com.musinsa.domain.product.service.BrandService;
import com.musinsa.global.common.response.BasicResponseDTO;
import com.musinsa.global.constants.swagger.SwaggerConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * Created by kimkh on 2024. 10. 29..
 */
@Tag(name = SwaggerConstants.brand.BRAND_TAG)
@RequestMapping(value = SwaggerConstants.brand.BRAND_V1)
@RequiredArgsConstructor
@RestController
public class BrandController {
    private final BrandService brandService;

    /**
     * 구현 2) - 단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 카테고리의 상품가격, 총액을 조회하는 API
     */
    @Operation(summary = SwaggerConstants.brand.BRAND_API_V1_DESC, description =
        SwaggerConstants.brand.BRAND_API_V1_VERSION + SwaggerConstants.brand.BRAND_API_V1_DESC, responses = {
        @ApiResponse(responseCode = SwaggerConstants.HTTP_STATUS_OK, description = SwaggerConstants.brand.BRAND_API_V1_DESC, content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = LowestPriceBrandDto.class))
        })
    })
    @GetMapping(value = SwaggerConstants.brand.BRAND_API_V1_URL)
    public Object brands() {
        return BasicResponseDTO.builder()
            .data(brandService.getLowestPriceProductsByBrand())
            .build();
    }
}
