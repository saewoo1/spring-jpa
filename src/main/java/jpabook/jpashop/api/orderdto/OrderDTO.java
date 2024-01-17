package jpabook.jpashop.api.orderdto;

import java.time.LocalDateTime;
import java.util.List;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;

@Data
public class OrderDTO {
    private Long orderId;
    private String name;
    private LocalDateTime localDateTime;
    private OrderStatus orderStatus;
    private Address address;
    private List<OrderItemDTO> orderItems;

    public OrderDTO(Order order) {
        this.orderId = order.getId();
        this.name = order.getMember().getName(); // Lazy 초기화 (getName) 호출 시
        this.localDateTime = order.getOrderDateTime();
        this.orderStatus = order.getStatus();
        this.address = order.getDelivery().getAddress(); // Lazy 초기화
//        order.getOrderItems().forEach(o -> o.getItem().getName()); // Lazy 강제 초기화
//        this.orderItems = order.getOrderItems(); // Entity
        this.orderItems = order.getOrderItems()
                .stream()
                .map(OrderItemDTO::new).toList();
    }
}
