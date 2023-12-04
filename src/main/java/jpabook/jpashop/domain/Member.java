package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue // sequence 값으로 쓸게
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address; // Address 클래스 @Embeddable -> JPA Entity의 private 선언 가능, Embedded or Embeddable 둘 중 하나만 써도 됨

    /*
    * 난 맵핑됐어. 주인 아님! 스스로 안바뀔거구
    * Order 테이블의 member 필드에 의해 맵핑된 애야!!
    * 난 걍 얘가 바뀌면 바뀌는 거울임. 읽기 전용!
    * orders에 뭘 더 추가한다고 해서 FK 영향 없음요
    * */
    @JsonIgnore
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
}
