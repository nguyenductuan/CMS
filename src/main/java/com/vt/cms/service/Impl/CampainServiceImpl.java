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
        campain.setCampainname(request.getCampainname());
        campain.setStatus("PendingApproved");
        campain.setStartdate(request.getStartdate());
        campain.setEnddate(request.getEnddate());
        campain.setDiscountpercent(request.getDiscountpercent());
        campainRepository.savecampain(campain);

        int campainId = campain.getCampainid();
        CampainProduct campainProduct = new CampainProduct();

        for (CampainProductRequest productcampain : request.getProducts() )
        {
            campainProduct.setCampainid(campainId);
            campainProduct.setProductId(productcampain.getProductid());
            for (CampainProductSku skuID: productcampain.getSkus())
            {

               campainProduct.setSkuId(skuID.getSkuId());
               campainProduct.setDiscountPrice(skuID.getDiscountPrice());
               campainProduct.setStockCampaign("10");
               campainProduct.setStatus(skuID.getStatus());
               campainProduct.setPrice(skuID.getDiscountPrice());
            }
            campainProductRepository.savecampainproduct(campainProduct);
        }
    }
// Luồng ập nhật trạng thái chiến dịch
    @Override
    public void updatestatus(int campainID, String status) {
        Campain campain = campainRepository.getCampainById(campainID);
        campain.setStatus(status);
        campainRepository.savecampain(campain);
    }

}