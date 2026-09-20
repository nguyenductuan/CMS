package com.vt.cms.service.Impl;

import com.vt.cms.model.dto.CampainProductRequest;
import com.vt.cms.model.dto.CampainProductSku;
import com.vt.cms.model.dto.CreatedCampainRequest;
import com.vt.cms.model.entity.Campain;
import com.vt.cms.model.entity.CampainProduct;

import com.vt.cms.model.repository.CampainProductRepository;
import com.vt.cms.model.repository.CampainRepository;
import com.vt.cms.model.repository.ProductRepository;
import com.vt.cms.service.CampainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CampainServiceImpl implements CampainService {
    private CampainRepository campainRepository;
    private CampainProductRepository campainProductRepository;

    public CampainServiceImpl(CampainRepository campainRepository,
                              CampainProductRepository campainProductRepository,
                              ProductRepository productRepository) {
        this.campainRepository = campainRepository;
        this.campainProductRepository = campainProductRepository;
    }

    @Override
    public void createdCampain(CreatedCampainRequest request) {
        Campain campain = new Campain();
        campain.setName(request.getName());
        campain.setStatus("PendingApproved");
        campain.setCreatedAt(request.getStartTime());
        campain.setCreatedBy("SYSTEM");
        campain.setIsDeleted("false");
        campain.setStartTime(request.getStartTime());
        campain.setEndTime(request.getEndTime());
        campain.setQuantity(10);
        campain.setTotalStock(100);
        campain.setDiscount(request.getDiscount());
        campain.setSalesCommission(request.getSalesCommission());
        System.out.println(campain);
        campainRepository.savecampain(campain);

        int campainId = campain.getCampainid();
        CampainProduct campainProduct = new CampainProduct();

        for (CampainProductRequest productcampain : request.getProducts() )
        {
            campainProduct.setCampaignId(campainId);
            campainProduct.setProductId(productcampain.getProductid());
            for (CampainProductSku skuID: productcampain.getSkus())
            {
               campainProduct.setSkuId(skuID.getSkuId());
               campainProduct.setPrice_discount(skuID.getDiscountPrice());
               campainProduct.setStockCampaign(skuID.getStock());
               campainProduct.setStatus(skuID.getStatus());
               campainProduct.setIsDeleted("false");
               campainProduct.setCreatedBy("SYSTEM");
               campainProduct.setPrice(skuID.getDiscountPrice());
            }
            campainProductRepository.savecampainproduct(campainProduct);
        }
    }
// Luồng cập nhật trạng thái chiến dịch
    @Override
    public void updatestatus(int campainID, String status) {
        Campain campain = campainRepository.getCampainById(campainID);
        campain.setStatus(status);
        campainRepository.savecampain(campain);
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