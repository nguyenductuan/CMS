package com.vt.cms.campain.application.service.port;

import java.util.Map;
import java.util.Set;

public interface CampaignProductPort {
  void updateStockSkus(Set<Integer> skuIds, Map<Integer, Integer> skuRestoreMap);
}
