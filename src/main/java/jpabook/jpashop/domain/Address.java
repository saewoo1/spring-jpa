package jpabook.jpashop.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable // 어딘가에 내장될 수 있다.
@Getter
public class Address {

    private String city;
    private String street;
    private String zipcode;

    /*
    * 값을 변경하면 안될 때. 무조건 넣으면 걘 고정으로 만들도록. 값타입은 변경 불가능하도록!
    * protected -> 이거는 그냥 JPA 스펙때문에 만듦.
    * JPA 기술 이용하려면 기본적으로 기본 생성자 있어야돼서
    * */
    protected Address() {
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
