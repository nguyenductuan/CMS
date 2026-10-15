package com.vt.cms.service.Impl;

import com.vt.cms.Exception.BusinessException;
import com.vt.cms.model.dto.*;
import com.vt.cms.model.entity.ProductDetail;
import com.vt.cms.model.entity.Shipping;
import com.vt.cms.model.enums.MessageCode;
import com.vt.cms.model.repository.ProductRepository;
import com.vt.cms.model.resp.SkuResponse;
import com.vt.cms.service.CheckoutService;
import com.vt.cms.service.PriceService;
import com.vt.cms.service.ShippingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CheckoutServiceImpl implements CheckoutService {

    private final ShippingService shippingService;
    private final ProductRepository productRepository;
    private final PriceService priceService;

    public CheckoutServiceImpl(ShippingService shippingService, ProductRepository productRepository, PriceService priceService) {
        this.shippingService = shippingService;
        this.productRepository = productRepository;
        this.priceService = priceService;
    }

    @Override

    public CheckoutPreviewResponse getCheckout(CheckoutPreviewRequest request) {
        // 1. Validate request
        validateRequest(request);
        // 2. Lấy thông tin sản phẩm + tính giá tiền
        List<ItemDto> iteemDtos = new ArrayList<>();
        BigDecimal totalprice = BigDecimal.ZERO;
        for (ItemRequest itemRequest : request.getItems()) {
            ItemDto itemDto = processItem(itemRequest);
            BigDecimal subtotal = priceService.calculateItemPrice(itemDto.getPrice_discount(), itemDto.getQuantity());
            totalprice = totalprice.add(subtotal);
            iteemDtos.add(itemDto);
        }
        //3. Lấy danh sách vận chuyển + lấy thông tin giá vận chuyển đầu tiên
        List<Shipping> shippingMethodDTO = shippingService.getShipping();
        if (shippingMethodDTO == null || shippingMethodDTO.isEmpty()) {
            throw new BusinessException(MessageCode.SHIPPING_NOT_FOUND);
        }
        BigDecimal shippingFree = shippingMethodDTO.get(0).getFee();
        totalprice = totalprice.add(shippingFree);
        /**
         * Build Checkout Respons
         **/
        CheckoutPreviewResponse response = new CheckoutPreviewResponse();
        response.setShipping_fee(shippingFree);
        response.setItems(iteemDtos);
        response.setTotalPrice(totalprice);
        //Lấy giá trị mặc định QR
        response.setPaymentMethod("QR");
        return response;
    }
    /**
     * Validate request checkout
     */
    private void validateRequest(CheckoutPreviewRequest request) {

        if (request == null) {
            throw new RuntimeException("Request không được null");
        }

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new RuntimeException("Danh sách sản phẩm không được để trống");
        }
    }

    /**
     * Lấy thông tin sku từ productId và validate số lượng tồn kho
     *
     * @return ItemDto
     */
    private ItemDto processItem(ItemRequest itemRequest) {

        ProductDetail product = productRepository.getproduct1(itemRequest.getProductId(), itemRequest.getCampainId());

        if (product == null) {
            throw new RuntimeException("Sản phẩm không tồn tại");
        }

        // Tìm SKU mà người dùng chọn
        SkuResponse sku = product.getSkus() .stream() .
                filter(x -> x.getId().equals(itemRequest.getSkuId())).findFirst()
                .orElseThrow(() -> new RuntimeException("SKU không tồn tại"));

        ItemDto itemDTO = new ItemDto();
        itemDTO.setProductId(product.getId());

       itemDTO.setCampaignId(itemRequest.getCampainId());
        itemDTO.setProductName(product.getName());
        itemDTO.setPrice_campain(sku.getPrice());
        itemDTO.setPrice_discount(sku.getPriceDiscountCampaign() != null ? sku.getPriceDiscountCampaign() : sku.getPrice());
        itemDTO.setQuantity(itemRequest.getQuantity());
       itemDTO.setStock(sku.getStock());
        return itemDTO;
    }
}
