package com.vt.cms.product.application.service;

import java.util.Map;
import java.util.Set;

public interface ProductSkuService {
  void updateStockSkus(Set<Integer> skuIds, Map<Integer, Integer> skuRestoreMap);
}
