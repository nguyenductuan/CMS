package com.vt.cms.service.Impl;

import com.vt.cms.model.dto.*;
import com.vt.cms.model.entity.Product;
import com.vt.cms.model.entity.Shipping;
import com.vt.cms.model.repository.ProductRepository;
import com.vt.cms.service.CheckoutService;
import com.vt.cms.service.ShippingService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CheckoutSerrviceImpl implements CheckoutService {

    private final ShippingService shippingService;
    private final ProductRepository productRepository;

    public CheckoutSerrviceImpl(ShippingService shippingService, ProductRepository productRepository) {

        this.shippingService = shippingService;
        this.productRepository = productRepository;
    }

    @Override
    public CheckoutPreviewResponse getCheckout(CheckoutPreviewRequest request) {
        //Khởi tạo item
        List<ItemDto> itemDtos = new ArrayList<>();
        double totalPrice = 0;
        //       1. Xử lý từng imtem trong request
        for (ItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.detailProduct(itemRequest.getProductId());

//            2.1. Check khi thông tin product không tồn tại
            if (product == null) {
                throw new RuntimeException("Sản phẩm không tồn tại");
            }
//                    2.2. Check tồn kho
            if (itemRequest.getQuantity() > product.getStock()) {
                throw new RuntimeException("Sản phẩm hết hàng");
            }
            // mapping sang DTO
            ItemDto itemDTO = new ItemDto();
            itemDTO.setProductId(product.getId());
            itemDTO.setProductName(product.getName());
            itemDTO.setPrice(product.getPrice());
            itemDTO.setQuantity(itemRequest.getQuantity());
            itemDTO.setStock(product.getStock());
            // tính tổng tiền đơn hàng  totalPrice
            double subtotal = product.getPrice() * itemRequest.getQuantity();
            totalPrice += subtotal;

            itemDtos.add(itemDTO);
        }
        //  lấy shipping methods
        List<Shipping> shippingList = shippingService.getShipping();

        List<ShippingMethodDTO> shippingDTOList = new ArrayList<>();

        for (Shipping s : shippingList) {
            ShippingMethodDTO dto = new ShippingMethodDTO();
            dto.setId(s.getServicecode());
            dto.setName(s.getServicename());
            dto.setEstimatedDelivery("2-3 ngày");
            dto.setOriginalFee(s.getFee());

            shippingDTOList.add(dto);
        }
        // 5. Chọn shipping mặc định (ví dụ: cái đầu tiên)
        int shippingFee = shippingDTOList.isEmpty() ? 0 : shippingDTOList.get(0).getOriginalFee();
        // 6. Tổng tiền = tiền đơn hàng + phí ship
        double total = totalPrice + shippingFee;
        // build response
        CheckoutPreviewResponse response = new CheckoutPreviewResponse();
        response.setItems(itemDtos);
        response.setShippingMethods(shippingDTOList);
        response.setTotalPrice(total);
        return response;
    }
}

