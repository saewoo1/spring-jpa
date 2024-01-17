package jpabook.jpashop.api.orderdto;

import jpabook.jpashop.domain.OrderItem;
import lombok.Data;

/**
 *                     "orderPrice": 20000,
 *                     "count": 3,
 *                     "totalPrice": 60000
 *
 */
@Data
public class OrderItemDTO {

    private String itemName;
    private int price;
    private int count;

    public OrderItemDTO(OrderItem orderItem) {
        this.itemName = orderItem.getItem().getName();
        this.price = orderItem.getOrderPrice();
        this.count = orderItem.getCount();
    }
}
