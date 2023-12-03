package jpabook.jpashop;

import jpabook.jpashop.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class MemberRepositoryTest {

    /*
     * EntityManager 통한 모든 변경들은
     * transaction 안에서 이루어져야 한다!!
     * 추가적으로 Transactional 어노테이션이 테스트 클래스 안에 있으면,
     * 테스트 종료 후 모든 데이터를 롤백한다.
     * 롤백하기시르믄 Rollback(false) 추가 -> 커밋 해버림
     * */
    @Autowired
//    MemberRepository memberRepository;

    @Transactional
    @Rollback(value = false)
    void save() {
        //given
        Member member = new Member();
//        member.setUserName("saewoo");

        //when
//        Long savedId = memberRepository.save(member);
//        Member findMember = memberRepository.find(savedId);
        //then
//        assertThat(findMember.getId()).isEqualTo(member.getId());
//        assertThat(findMember.getUserName()).isEqualTo(member.getUserName());

        /*
         * 저장한 것 == 조회한 것 이게 왜 똑같아?
         * 같은 영속성 컨텍스트(하나의 transaction) 안에서는
         * id(식별자) 값이 같으면 같은 entity로 식별하기 때문
         * */
//        assertThat(findMember).isEqualTo(member);
    }
}