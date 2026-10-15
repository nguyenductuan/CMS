package com.vt.cms.service.Impl;

import com.vt.cms.Exception.BusinessException;
import com.vt.cms.model.dto.*;
import com.vt.cms.model.entity.ProductDetail;
import com.vt.cms.model.entity.Shipping;
import com.vt.cms.model.enums.MessageCode;
import com.vt.cms.model.repository.ProductRepository;
import com.vt.cms.model.resp.SkuResponse;
import com.vt.cms.service.CheckoutService;
import com.vt.cms.service.OrderPricingService;
import com.vt.cms.service.ShippingService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CheckoutServiceImpl implements CheckoutService {
    private final ShippingService shippingService;
    private final ProductRepository productRepository;
    private final OrderPricingService orderPricingService;

    public CheckoutServiceImpl(ShippingService shippingService,
                               ProductRepository productRepository,
                               OrderPricingService orderPricingService) {
        this.shippingService = shippingService;
        this.productRepository = productRepository;
        this.orderPricingService = orderPricingService;
    }

    @Override
    public CheckoutPreviewResponse getCheckout(CheckoutPreviewRequest request) {
        validateRequest(request);

        List<ItemDto> items = new ArrayList<>();
        BigDecimal productTotal = BigDecimal.ZERO;
        for (ItemRequest requestedItem : request.getItems()) {
            ItemDto item = processItem(requestedItem);
            productTotal = productTotal.add(orderPricingService.calculateLineTotal(
                    item.getPrice_discount(), item.getQuantity()));
            items.add(item);
        }

        List<Shipping> shippingMethods = shippingService.getShipping();
        if (shippingMethods == null || shippingMethods.isEmpty()
                || shippingMethods.get(0) == null || shippingMethods.get(0).getFee() == null) {
            throw new BusinessException(MessageCode.SHIPPING_NOT_FOUND);
        }

        // Preserve existing API behavior: preview uses the first configured shipping fee.
        BigDecimal shippingFee = shippingMethods.get(0).getFee();
        CheckoutPreviewResponse response = new CheckoutPreviewResponse();
        response.setShipping_fee(shippingFee);
        response.setItems(items);
        response.setTotalPrice(productTotal.add(shippingFee));
        response.setPaymentMethod("QR");
        return response;
    }

    private void validateRequest(CheckoutPreviewRequest request) {
        if (request == null || request.getItems() == null || request.getItems().isEmpty()) {
            throw new BusinessException(MessageCode.INVALID_ORDER);
        }
        for (ItemRequest item : request.getItems()) {
            if (item == null || item.getProductId() == null || item.getProductId() <= 0
                    || item.getCampainId() == null || item.getSkuId() == null
                    || item.getQuantity() <= 0) {
                throw new BusinessException(MessageCode.INVALID_ORDER);
            }
        }
    }

    private ItemDto processItem(ItemRequest request) {
        ProductDetail product = productRepository.getproduct1(
                request.getProductId(), request.getCampainId());
        if (product == null) {
            throw new BusinessException(MessageCode.PRODUCT_NOT_FOUND);
        }

        List<SkuResponse> skus = product.getSkus();
        SkuResponse sku = skus == null ? null : skus.stream()
                .filter(candidate -> candidate != null
                        && Objects.equals(candidate.getId(), request.getSkuId()))
                .findFirst().orElse(null);
        if (sku == null) {
            throw new BusinessException(MessageCode.PRODUCT_NOT_FOUND);
        }
        if (sku.getStock() == null || request.getQuantity() > sku.getStock()) {
            throw new BusinessException(MessageCode.STOCK_NOT_ENOUGH);
        }

        ItemDto item = new ItemDto();
        item.setProductId(product.getId());
        item.setCampaignId(request.getCampainId());
        item.setProductName(product.getName());
        item.setPrice_campain(sku.getPrice());
        item.setPrice_discount(orderPricingService.getUnitPrice(sku));
        item.setQuantity(request.getQuantity());
        item.setStock(sku.getStock());
        return item;
    }
}
