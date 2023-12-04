package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UpgradeOrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = {"member", "delivery"})
    List<Order> findAll();
}
