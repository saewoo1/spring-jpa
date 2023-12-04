package jpabook.jpashop.api.orderdto;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderSimpleDTO {

    private Long orderId;
    private String name;
    private LocalDateTime localDateTime;
    private OrderStatus orderStatus;
    private Address address;

    public OrderSimpleDTO(Order order) {
        this.orderId = order.getId();
        this.name = order.getMember().getName(); // Lazy 초기화 (getName) 호출 시
        this.localDateTime = order.getOrderDateTime();
        this.orderStatus = order.getStatus();
        this.address = order.getDelivery().getAddress(); // Lazy 초기화
    }

    public OrderSimpleDTO(Long orderId, String name, LocalDateTime localDateTime, OrderStatus status, Address address) {
        this.orderId = orderId;
        this.name = name; // Lazy 초기화 (getName) 호출 시
        this.localDateTime = localDateTime;
        this.orderStatus = status;
        this.address = address; // Lazy 초기화
    }
}
