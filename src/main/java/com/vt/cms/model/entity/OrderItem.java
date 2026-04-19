package com.vt.cms.model.entity;

import lombok.Data;

@Data
public class OrderItem{
    private Long productId;
    private Integer quantity;
    private Double price;
}


//
//
//public class OrderService {
//
//    private ProductMapper productMapper;
//    private OrderMapper orderMapper;
//    private OrderItemMapper orderItemMapper;
//
//    public void createOrder(List<OrderItem> items) {
//
//        double total = 0;
//
//        // kiểm tra + tính tiền
//        for (OrderItem item : items) {
//            Product product = productMapper.findById(item.getProductId());
//
//            if (product == null) {
//                throw new RuntimeException("Sản phẩm không tồn tại");
//            }
//
//            if (product.getStock() < item.getQuantity()) {
//                throw new RuntimeException("Không đủ hàng");
//            }
//
//            total += product.getPrice() * item.getQuantity();
//            item.setPrice(product.getPrice());
//        }
//
//        // tạo order
//        Order order = new Order();
//        order.setTotal(total);
//        orderMapper.insertOrder(order);
//
//        // insert order item + trừ kho
//        for (OrderItem item : items) {
//
//            productMapper.updateStock(item.getProductId(), item.getQuantity());
//
//            orderItemMapper.insertOrderItem(
//                    order.getId(),
//                    item.getProductId(),
//                    item.getQuantity(),
//                    item.getPrice()
//            );
//        }
//    }
//}
//
//
//
//file xml
//        <mapper namespace="ProductMapper">
//
//    <select id="findById" resultType="Product">
//SELECT * FROM product WHERE id = #{id}
//    </select>
//
//    <update id="updateStock">
//UPDATE product
//SET stock = stock - #{quantity}
//WHERE id = #{id} AND stock >= #{quantity}
//    </update>
//
//</mapper>
//<mapper namespace="OrderMapper">
//
//    <insert id="insertOrder" useGeneratedKeys="true" keyProperty="id">
//INSERT INTO orders(total)
//VALUES(#{total})
//    </insert>
//
//</mapper>
//OrderItemMapper.xml
//        <mapper namespace="OrderItemMapper">
//
//        <insert id="insertOrderItem">
//INSERT INTO order_item(order_id, product_id, quantity, price)
//VALUES(#{orderId}, #{productId}, #{quantity}, #{price})
//    </insert>
//
//</mapper>
