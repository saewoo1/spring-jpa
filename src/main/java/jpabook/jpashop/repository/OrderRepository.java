package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import java.util.List;
import jpabook.jpashop.api.orderdto.OrderSimpleDTO;
import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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

    public List<Order> findAllWithItem() {
        return em.createQuery(
                "select distinct o from Order o"
                        + " join fetch o.member m"
                        + " join fetch o.delivery d"
                        + " join fetch o.orderItems oi"
                        + " join fetch oi.item i", Order.class
        ).getResultList();
    }
    /*
    * distinct -> 같은 식별자라면 중복 제거
    * 일대 다(orderItems) 조회라서 다 쪽을 따라가버림 DB에는 여전히 4개지만, entity 기준으로 중복만 제거
    *
    * 단점 -> 페이징 불가능;;
    * db에서 가져온 정보는 여전히 4개라서 시작, 끝점을 몰라.. -> 페이징이 불가능하다 베리 위험!
    *
    * 컬렉션 fetch join 은 1개만 쓸 수 있다
    * 컬렉션 둘 이상에 페치 조인 쓰지마라. 데이터 부정확하게 조회될 위험이 있다.
    * 일대 다대 다 ;;;
    * */

    public List<Order> findAllWithMemberDelivery(int offset, int limit) {
        return em.createQuery(
                "select o from Order o " +
                        "join fetch o.member m " +
                        "join fetch o.delivery d", Order.class)
                .getResultList();
    }

}
