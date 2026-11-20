package com.vt.cms.campain.infrastructure.adapter;

import com.vt.cms.campain.application.service.port.CampaignProductPort;
import com.vt.cms.product.application.service.ProductSkuService;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class CampaignProductAdapter implements CampaignProductPort {
  final ProductSkuService productSkuService;

  public CampaignProductAdapter(ProductSkuService productSkuService) {
    this.productSkuService = productSkuService;
  }

  @Override
  public void updateStockSkus(Set<Integer> skuIds, Map<Integer, Integer> skuRestoreMap) {
    productSkuService.updateStockSkus(skuIds, skuRestoreMap);
  }
}
