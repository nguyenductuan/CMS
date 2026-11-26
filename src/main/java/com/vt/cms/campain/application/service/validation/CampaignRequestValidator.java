package com.vt.cms.campain.application.service.validation;

import com.vt.cms.Exception.BusinessException;
import com.vt.cms.base.enums.MessageCode;
import com.vt.cms.campain.application.dto.request.CampainProductRequest;
import com.vt.cms.campain.application.dto.request.CampainProductSku;
import com.vt.cms.campain.application.dto.request.CreatedCampainRequest;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class CampaignRequestValidator {
  /** Kiểm tra tính hợp lệ validate. Tham số và kết quả tuân theo chữ ký phương thức. */
  public void validate(CreatedCampainRequest request) {
    if (request == null) throw new BusinessException(MessageCode.REQUEST_IS_REQUIRED);
    if (request.getName() == null || request.getName().isBlank())
      throw new BusinessException(MessageCode.TEN_CHIEN_DICH_KHONG_DUOC_DE_TRONG);
    if (request.getStartTime() == null || request.getEndTime() == null)
      throw new BusinessException(MessageCode.START_TIME_AND_END_TIME_ARE_REQUIRED);
    if (request.getStartTime().isAfter(request.getEndTime()))
      throw new BusinessException(MessageCode.START_TIME_MUST_BE_BEFORE_OR_EQUAL_TO_END_TIME);
    validateDiscount(request.getDiscount());
    if (request.getProducts() == null || request.getProducts().isEmpty())
      throw new BusinessException(MessageCode.CAMPAIGN_MUST_CONTAIN_AT_LEAST_ONE_PRODUCT);
    Set<Integer> requestedSkuIds = new HashSet<>();
    for (CampainProductRequest product : request.getProducts()) {
      if (product == null || product.getProductid() <= 0)
        throw new BusinessException(MessageCode.PRODUCT_ID_MUST_BE_VALID);
      if (product.getSkus() == null || product.getSkus().isEmpty())
        throw new BusinessException(MessageCode.EACH_CAMPAIGN_PRODUCT_MUST_CONTAIN_AT_LEAST_ONE_);
      for (CampainProductSku sku : product.getSkus()) {
        if (sku == null || sku.getSkuId() == null)
          throw new BusinessException(MessageCode.SKU_ID_IS_REQUIRED);
        if (!requestedSkuIds.add(sku.getSkuId()))
          throw new BusinessException(MessageCode.DUPLICATE_SKU_ID);
      }
    }
    // TODO:Check thời gian

    //    if (!request.getStartTime().isBefore(request.getEndTime())
    //        || request.getStartTime() == null
    //        || request.getEndTime() == null
    //        || request.getStartTime().isBefore(LocalDateTime.now())) {
    //      throw new BusinessException(MessageCode.TIME_INVALID);
    //    }
  }

  /** Kiểm tra tính hợp lệ validate discount. Tham số và kết quả tuân theo chữ ký phương thức. */
  private void validateDiscount(BigDecimal discount) {
    if (discount == null) throw new BusinessException(MessageCode.DISCOUNT_IS_REQUIRED);
    if (discount.stripTrailingZeros().scale() > 0)
      throw new BusinessException(MessageCode.DISCOUNT_MUST_BE_AN_INTEGER);
    if (discount.compareTo(BigDecimal.ZERO) < 0 || discount.compareTo(BigDecimal.valueOf(100)) > 0)
      throw new BusinessException(MessageCode.DISCOUNT_MUST_NOT_EXCEED_100);
  }
}
