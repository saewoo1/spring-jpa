package jpabook.jpashop.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.util.AssertionErrors.assertEquals;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.exception.NotEnoughStockException.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;
    // 상품 주문 테스트
    @Test
    void order() {
        Member member = createMember("saewoo");
        Book book = createBoot(12345, "감자탕", 10);

        int count = 3;
        Long orderId = orderService.order(member.getId(), book.getId(), count);
        Order order = orderRepository.findOne(orderId);

        assertEquals("상품 주문 시 상태는 Order", OrderStatus.ORDER, order.getStatus());
        assertEquals("주문 상품 수가 정확해야한다.", 1, order.getOrderItems().size());
        assertEquals("총 가격 ", 12345 * 3 , order.getTotalPrice());
        assertEquals("주문 후 재고 줄어?", 7, book.getStockQuantity());

    }

    @Test
    void overQuantityOrder() {
        Member member = createMember("saewoo");
        Book book = createBoot(12345, "감자탕", 10);

        int count = 11;
        assertThatThrownBy(() ->
                orderService.order(member.getId(), book.getId(), count))
                .isInstanceOf(NotEnoughStockException.class);
    }

    private Book createBoot(int price, String name, int quantity) {
        Book book = new Book();
        book.setPrice(price);
        book.setName(name);
        book.setStockQuantity(quantity);
        em.persist(book);
        return book;
    }

    private Member createMember(String name) {
        Member member = new Member();
        member.setName(name);
        member.setAddress(new Address("서울", "어딘가", "123-123"));
        em.persist(member);
        return member;
    }

    // 상품 취소 테스트
    @Test
    void cancelOrder() {
        Member member = createMember("냠냠");
        Book book = createBoot(20000, "안녕하세요", 30);

        Long orderId = orderService.order(member.getId(), book.getId(), 3);

        orderService.cancelOrder(orderId);
        Order order = orderRepository.findOne(orderId);

        assertEquals("주문 취소 후 상태는 CANCELED", OrderStatus.CANCEL, order.getStatus());
        assertEquals("주문 취소 시 재고 복구", 30, book.getStockQuantity());
    }
}