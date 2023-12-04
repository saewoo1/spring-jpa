package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.api.orderdto.OrderSimpleDTO;
import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    public List<Order> findAll() {
        return em.createQuery("select o from Order o", Order.class)
                .getResultList();
    }

    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery(
                "select o from Order o " +
                        "join fetch o.member m " +
                        "join fetch o.delivery d", Order.class
        ).getResultList();
    }
    /*
    * join fetch
    * order 조회할 때 member 객체까지 가져와
    * sql 입장에서는
    * order, member, delivery 조인 -> select 절에 다 넣어서 한번에 다 땡겨옴
    * 프록시도 아니고, 진짜 객체 값들이니까 Lazy 초기화 안해~
    *
    * fetch join : order -> member, order -> delivery 이미 조회된 상태라 지연로당 X
    * */

    public List<OrderSimpleDTO> findOrderDTOs() {
        return em.createQuery(
                "select new jpabook.jpashop.api.orderdto.OrderSimpleDTO(o.id, m.name, o.orderDateTime, o.status, d.address)" +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d", OrderSimpleDTO.class
        ).getResultList();
    }
    /*
    * 필요한 값만 가져오는 직접 짠 쿼리문이지만, -> 성능 최적화
    * 다른 코드에는 재사용하기 어렵다.
    *
    * API 스펙에 맞게 짠 코드가 되어버린다.
    * */
}
