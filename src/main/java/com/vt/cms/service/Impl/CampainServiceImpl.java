package com.vt.cms.service.Impl;

import com.vt.cms.model.dto.CampainProductRequest;
import com.vt.cms.model.dto.CampainProductSku;
import com.vt.cms.model.dto.CreatedCampainRequest;
import com.vt.cms.model.entity.Campain;
import com.vt.cms.model.entity.CampainProduct;
import com.vt.cms.model.entity.CampainproductSku;
import com.vt.cms.model.entity.Product;
import com.vt.cms.model.repository.CampainRepository;
import com.vt.cms.model.repository.ProductRepository;
import com.vt.cms.service.CampainService;

public class CampainServiceImpl implements CampainService {
    private CampainRepository campainRepository;

    public CampainServiceImpl(CampainRepository campainRepository, ProductRepository productRepository) {
        this.campainRepository = campainRepository;
    }

    @Override
    public void createdCampain(CreatedCampainRequest request) {
        Campain campain = new Campain();
        campain.setCampainname(request.getCampainname());
        campain.setStartdate(request.getStartdate());
        campain.setEnddate(request.getEnddate());
        campain.setDiscountpercent(request.getDiscountpercent());

        campainRepository.savecampain(campain);
        long campainId = campain.getCampainid();
        CampainProduct campainProduct = new CampainProduct();
        CampainproductSku campainproductSku = new CampainproductSku();
        for (CampainProductRequest campainProductRequest : request.getProducts() )
        {
            campainProduct.setCampainid(campainId);
            campainProduct.setProductId(campainProductRequest.getProductid());
            campainProductRepository.save(campainProduct);
            long campainProductId = campainProduct.getProductId();
            for (CampainProductSku campainProductSkurequest: campainProductRequest.getSkus())
            {
                campainproductSku.setProductID(campainProductId);
                campainproductSku.setDiscountPrice();
                campainproductSku.setStatus();
                campainProductSkuRepository.save(campainproductSku);
            }
        }
    }
}