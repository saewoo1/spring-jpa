package jpabook.jpashop.api;

import jpabook.jpashop.api.orderdto.OrderDTO;
import jpabook.jpashop.api.orderdto.OrderResult;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.order.query.OrderQueryRepository;
import jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> orders = orderRepository.findAll();
//         Lazy 지연 때문에 한번씩 건들여야됨 -> Hibernate5Module 통해서 Proxy가 아닌 정상적으로 초기화된 애들만 (Lazy=null)
        for (Order order : orders) {
            order.getMember().getName();
            order.getDelivery().getAddress();
            List<OrderItem> items = order.getOrderItems();
            items.forEach(orderItem -> orderItem.getItem().getName());
        }

        return orders;
    }

    @GetMapping("/api/v2/orders")
    public OrderResult ordersV2() {
        List<OrderDTO> orderDTOS = orderRepository.findAll().stream()
                .map(OrderDTO::new)
                .toList();
        // OrderItem이 entity 라서 foreach 돌면서 안찔러주면 Lazy라 null로 보여짐
        return new OrderResult(orderDTOS.size(), orderDTOS);
    }

    @GetMapping("/api/v3/orders")
    public OrderResult orderV3() {
        List<Order> orders = orderRepository.findAllWithItem();
        List<OrderDTO> orderDTOS = orderRepository.findAll().stream()
                .map(OrderDTO::new)
                .toList();
        return new OrderResult(orderDTOS.size(), orderDTOS);
    }

    @GetMapping("/api/v3.1/orders")
    public OrderResult ordersV3_page(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "100") int limit
    ) {
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
        List<OrderDTO> orderDTOS = orders.stream()
                .map(OrderDTO::new)
                .toList();
        return new OrderResult(orderDTOS.size(), orderDTOS);
    }

    @GetMapping("/api/v4/orders")
    public OrderResult ordersV4() {
        return orderQueryRepository.findOrderQueryDtos();
    }
}
