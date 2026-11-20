package com.vt.cms.product.application.service.Impl;

import com.vt.cms.product.application.service.ProductSkuService;
import com.vt.cms.product.domain.entity.Product_Sku;
import com.vt.cms.product.domain.repository.ProductSkuRepository;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class ProductSkuServiceimpl implements ProductSkuService {
  final ProductSkuRepository productSkuRepository;

  public ProductSkuServiceimpl(ProductSkuRepository productSkuRepository) {
    this.productSkuRepository = productSkuRepository;
  }

  @Override
  public void updateStockSkus(Set<Integer> skuIds, Map<Integer, Integer> skuRestoreMap) {
    List<Product_Sku> skus = productSkuRepository.findSkuByIds(skuIds);

    // UPDATE STOCK
    for (Product_Sku sku : skus) {
      int currentStock = sku.getStock() != null ? sku.getStock() : 0;
      int restore = skuRestoreMap.getOrDefault(sku.getId(), 0);

      int newStock = currentStock + restore;

      sku.setStock(newStock);
    }
    System.out.println("C" + skus);
    productSkuRepository.saveSkus(skus);
  }
}
