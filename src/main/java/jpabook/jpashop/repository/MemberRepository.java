package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import java.util.List;
import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    // PersistenceContext -> 스프링부트가 @Autowired 제공, @RequiredArgsConstructor 쓸수잇어
    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    /*
    * jpql -> 객체 Entity 대상으로 search
    * */
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    /*
    * 특정 구간 지정
    * where m.name = :name
    * :name -> 파라미터 바인딩 대상
    * setParameter("name", name) 여기에서 바인딩이 일어남
    * */
    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name).getResultList();
    }

}
