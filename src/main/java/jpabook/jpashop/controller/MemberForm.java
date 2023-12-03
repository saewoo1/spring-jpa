package jpabook.jpashop.controller;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberForm {

    @NotEmpty(message = "회원 이름은 비어있을 수 없습니다.")
    private String name;

    private String city;
    private String street;
    private String zipcode;
}
