package com.musinsa.domain.product.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.musinsa.domain.product.dto.BrandLowestPriceDto;
import com.musinsa.domain.product.entity.BrandInfoEntity;
import com.musinsa.domain.product.repository.Custom.BrandInfoRepositoryCustom;

/**
 * Created by kimkh on 2024. 10. 29..
 */
@Repository
public interface BrandInfoRepository extends JpaRepository<BrandInfoEntity, String>, BrandInfoRepositoryCustom {
	void deleteByBrand(String brand);
	Optional<BrandInfoEntity> findByBrand(String brand);

}
