package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor // final 필드만 가지고 생성자 주입을 한다
public class MemberService {

//    @Autowired // 필드 인젝션 -> 테스트하기 힘드러.. private이야..
    private final MemberRepository memberRepository;
    // 생성자 인젝션 권장! 추가적으로 생성자가 단 하나만 있을 땐, Autowired 생략 가능하다

    // 회원 가입
    @Transactional // em 써서 데이터 변동이 있다면 꼭 트랜잭셔널!! 보통 스프링꺼 씀
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);

        return member.getId();
    }

    // 조회만 하는 경우 좀 더 최적화
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    private void validateDuplicateMember(Member member) {
        /*
        * 멀티쓰레드에서 동시 접근 시 문제가 될 수 있으니(ㄹㅇ 진짜 동시로 회원가입) -> 그럼 터져
        * 실무에서는 최후의 방어를 위해 추가적인 로직이 필요하다
        * ex) db의 Name 을 unique 제약 조건으로 잡는 등등..
        * */
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findOne(id);

        member.setName(name);
    }
    // 회원 전체 조회
}
