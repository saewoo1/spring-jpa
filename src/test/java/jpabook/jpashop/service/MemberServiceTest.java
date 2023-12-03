package jpabook.jpashop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional // 롤백할래. 커밋 안해 -> insert 문 안쏴. DB에 안올려.
class MemberServiceTest {

    @Autowired MemberService memberService; // 스프링 필드에 등록 스프링 컨테이너 안에서 돌릴래
    @Autowired MemberRepository memberRepository;

    @Test
//    @Rollback(value = false) 그래두 DB에 넣어볼래
    void 회원_가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("saewoo");
        //when
        Long savedId = memberService.join(member); // repo -> 등록

        //then
        //1. 새로만든 member, 등록한 member 동일해?
        /*
        * 이게 가능한 이유?
        * TestClass 상단에 Transactional 어노테이션 내부에서는
        * PK 값으로 하나의 영속성을 관리한다.
        * */
        assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test
    void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("saewoo1");

        Member member2 = new Member();
        member2.setName("saewoo1");

        //when
        memberService.join(member1);

        //then
        assertThrows(IllegalStateException.class, () -> {
            memberService.join(member2);
        });
//        fail("예외가 발생해서 밖으로 나가야한다. 여기까지 오면 안돼!");
    }
}