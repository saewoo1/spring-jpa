package jpabook.jpashop.api;

import jpabook.jpashop.api.orderdto.OrderResult;
import jpabook.jpashop.api.orderdto.OrderSimpleDTO;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
//    private final UpgradeOrderRepository upgradeOrderRepository;


    /*
    * 엔티티 직접 노출하지 말자!
    * @JsonIgnore 사용해서 양방향 연관관계는 이걸 억지로 무시해야되고, -> 안하면 서로 호출해서 무한루프
    * Hibernate5Module 을 사용해서 지연로딩을 피하는 방식도 있다 -> 얘도 비추 그냥 DTO를 만들어
    * */
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {

        List<Order> orders = orderRepository.findAll();
        for (Order order : orders) {
            order.getMember().getName(); // getMember까지는 가짜라서 DB 쿼리 안날리는데 getName 호출 시점에서 Lazy 강제 초기화
            order.getDelivery().getAddress();
        }

        return orders;
    }

    @GetMapping("/api/v2/simple-orders")
    public OrderResult ordersV2() {
        List<OrderSimpleDTO> result = orderRepository.findAll().stream()
                .map(OrderSimpleDTO::new)
                .toList();
        return new OrderResult(result.size(), result);
    }
    /*
    * Lazy 로딩의 순서 - 총 주문의 개수는 2개
    * 1. 첫 주문 꺼 DTO (order, member, delivery) 조회 ->
    *  1-1. 3회 쿼리, getMember, getDelivery 뒤의 getName, address 실행 시 Lazy 초기화!!
    * 2. 두번 째꺼 DTO -> Lazy 초기화 되었으니 다시 getName, getAddress 하기 위한 쿼리 2회
    *
    * 총 5회의 쿼리. N + 1 문제
    * 1 + 회원 N + 배송 N
    * 지연로딩은 영속성 컨텍스트에서 조회하므로, 이미 조회된 경우 쿼리를 생략한다.
    * */
    
    @GetMapping("/api/v3/simple-orders")
    public OrderResult ordersV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
//        List<Order> orders = upgradeOrderRepository.findAll();

        List<OrderSimpleDTO> result = orders.stream()
                .map(OrderSimpleDTO::new).toList();

        return new OrderResult(result.size(), result);
    }

    @GetMapping("/api/v4/simple-orders")
    public OrderResult ordersV4() {
        List<OrderSimpleDTO> orderDTOs = orderRepository.findOrderDTOs();
        // select 풀이 훨씬 좁아지넹 근데 다른데 쓰긴 어렵겠군..
        return new OrderResult<>(orderDTOs.size(), orderDTOs);
    }
}
