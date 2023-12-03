package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import java.util.List;
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

    // orderSearch 의 member 동일 && status 동일
//    public List<Order> findBySearch(OrderSearch orderSearch) {
//        return em.createQuery("select o from Order o join o.member m "
//                        + "where o.status = :status"
//                        + "and m.name like :name", Order.class)
//                .setParameter("name", orderSearch.getMemberName())
//                .setParameter("status", orderSearch.getOrderStatus())
//                .setMaxResults(1000)
//                .getResultList();

//        String jpql = "select o from Order o join o.member  m";
//        em.createQuery(jpql, Order.class);
//    }
}
