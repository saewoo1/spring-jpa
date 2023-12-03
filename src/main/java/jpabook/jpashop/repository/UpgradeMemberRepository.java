package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UpgradeMemberRepository extends JpaRepository<Member, Long> {
}
