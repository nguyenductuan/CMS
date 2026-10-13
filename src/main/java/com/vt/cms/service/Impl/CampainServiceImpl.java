package com.vt.cms.service.Impl;

import com.vt.cms.Exception.BusinessException;
import com.vt.cms.model.dto.CampainProductRequest;
import com.vt.cms.model.dto.CampainProductSku;
import com.vt.cms.model.dto.CreatedCampainRequest;
import com.vt.cms.model.entity.Campain;
import com.vt.cms.model.entity.CampainProduct;

import com.vt.cms.model.entity.Product;
import com.vt.cms.model.entity.Product_Sku;
import com.vt.cms.model.enums.CampainStatus;
import com.vt.cms.model.enums.MessageCode;
import com.vt.cms.model.repository.CampainProductRepository;
import com.vt.cms.model.repository.CampainRepository;
import com.vt.cms.model.repository.ProductRepository;
import com.vt.cms.model.repository.ProductSkuRepository;
import com.vt.cms.service.CampainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class CampainServiceImpl implements CampainService {
    private final ProductSkuRepository productSkuRepository;
    private CampainRepository campainRepository;
    private CampainProductRepository campainProductRepository;

    public CampainServiceImpl(CampainRepository campainRepository,
                              CampainProductRepository campainProductRepository,
                              ProductRepository productRepository, ProductSkuRepository productSkuRepository) {
        this.campainRepository = campainRepository;
        this.campainProductRepository = campainProductRepository;
        this.productSkuRepository = productSkuRepository;
    }

    @Override
    public void createdCampain(CreatedCampainRequest request) {
        Campain campain = new Campain();
        campain.setName(request.getName());
        campain.setStatus(CampainStatus.PENDING_APPROVAL.getMessage());
        campain.setStartTime(request.getStartTime());
        campain.setCreatedAt(LocalDateTime.now());
        campain.setDescription(request.getDescription());
        campain.setCreatedBy("SYSTEM");
        campain.setIsDeleted("false");
        campain.setStartTime(request.getStartTime());
        campain.setEndTime(request.getEndTime());
        campain.setTotalStock(request.getTotal_stock());
        campain.setDiscount(request.getDiscount());
//        campain.setSalesCommission(request.getSalesCommission());
        validateCampaign(request);
        validateDiscount(request.getDiscount());
        campainRepository.savecampain(campain);
        int campainId = campain.getCampainid();
        CampainProduct campainProduct = new CampainProduct();

        for (CampainProductRequest productcampain : request.getProducts() )
        {
            //Kiểm tra product có tồn tại trong hệ thống hay không và trạng thái product là ACTIVE hay không
            // ----------------------------------------------------------------------------------//
          List<Product_Sku> product = productSkuRepository.findByProductId(productcampain.getProductid());
            if (product == null) {
                log.warn("Product with ID {} does not exist", productcampain.getProductid());
                continue;
            }
            // ----------------------------------------------------------------------------------//

            campainProduct.setCampaignId(campainId);
            campainProduct.setProductId(productcampain.getProductid());
            for (CampainProductSku skuID: productcampain.getSkus())
            {
                //--------------------------------------------------------------------//
                //1. Kiểm tra sku có tồn tại trong hệ thống hay không
                if (skuID == null || !productRepository.existsBySkuId(skuID.getSkuId())) {
                    throw new BusinessException(MessageCode.PRODUCT_SKU_NOT_FOUND);
                }
              // 2. Kiểm tra SKU có đang ACTIVE
                if (!productRepository.isSkuActiveBySkuId(skuId)) {
                    throw new BusinessException(MessageCode. SKU_NOT_ACTIVE);
                }
                // 3. Kiểm tra tồn kho campaign
                Integer campaignStock = skuID.getStock_campaign();
                Integer availableStock = productRepository.getStockBySkuId(skuId);

                if (campaignStock == null || campaignStock < 0) {
                    throw new BusinessException(MessageCode.STOCK_INVALID);

                }

                if (availableStock == null || campaignStock > availableStock) {
                    throw new  BusinessException(MessageCode.STOCK_INSUFFICIENT);
                }
                //--------------------------------------------------------------------//
               campainProduct.setSkuId(skuID.getSkuId());
               campainProduct.setPrice_discount(skuID.getDiscountPrice());
               campainProduct.setStockCampaign(skuID.getStock_campaign());
               campainProduct.setStatus(skuID.getStatus());
               campainProduct.setIsDeleted("false");
               campainProduct.setCreatedBy("SYSTEM");
               campainProduct.setPrice(skuID.getPrice());
                campainProductRepository.savecampainproduct(campainProduct);
            }

        }
    }
    private void validateCampaign(CreatedCampainRequest request) {
        if (request.getStartTime() == null
                || request.getEndTime() == null) {
            throw new IllegalArgumentException(
                    "Start time and end time are required");
        }

        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new IllegalArgumentException(
                    "Invalid campaign time: startTime must be <= endTime");
        }
    }

    private void validateDiscount(BigDecimal discount) {
        if (discount == null) {
            throw new IllegalArgumentException("Discount is required");
        }

        if (discount.stripTrailingZeros().scale() > 0) {
            throw new IllegalArgumentException(
                    "Discount must be an integer");
        }

        if (discount.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new IllegalArgumentException(
                    "Discount must be <= 100%");
        }
    }

// Luồng cập nhật trạng thái chiến dịch
    @Override
    public void updatestatus(Integer campain_id, String status) {

//        campainRepository.savecampain(campain_id, status);
    }

    @Override
    public void getlist() {
     campainRepository.getlistcampain();
    }

    @Override
    public void deletecampain(Integer campainID) {
     campainRepository.deletecampain(campainID);
    }

}