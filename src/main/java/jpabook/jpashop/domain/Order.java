package jpabook.jpashop.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    /*
    * Order <-> Member
    *  다          일
    *
    * @JoinColumn -> FK 이름 member_id 설정
    * 주로 FK 가 있는 쪽(가까운 쪽)이 주인이다.
    * 이유는 자동차와 바퀴 이야기를 떠올려보셈
    * 자동차가 더 우위같은데? -> 자동차 니가 주인 이럼 ㅈ댄다
    * 바퀴랑 관련 없는 자동차 문같은거 바꿨더니 바퀴가 변경되거나 이래버려
    *
    * 주인 아닌쪽에 가서 나 주인 아님. 걍 거울이에요 선언하면 된다.
    *
    * Order 의 member 를 변경할 시 member_id FK 값이 다른 멤버로 변경된다
    * */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    /*
    * cascade -> persist 전파.
    * order persist -> orderItems 얘네도 자동 저장
    * 원래는 orderItemA ,B, C  다 persist 한 후 order persist 해야되는데
    * 그냥 order만 persist 하면 끝 delete 시에도 적용된다.
    * 얘가 없다면, Entity 마다 각자 persist 해야한다!
    * */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();
    /*
    * 왜 필드에서 초기화 하는가? (생성자 호출 시, 외부 주입 등등..방법이 다양한데.)
    * 1. NPE 에서 안전하다
    * 2. 하이버네이트의 엔티티 영속화 시, 컬렉션을 감싸서 하이버네이트가 제공하는 내장 컬렉션으로 변경.
    * 만약 임의의 메서드에서 컬렉션을 잘못 생성한다면, 내부 메커니즘에 문제 발생 위험이 있다.
    * ArrayList -> (em.persist(orderItems)) -> hibernate.collection~~PersistentBag ..
    * */

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDateTime; // 주문시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상태 [ORDER, CANCEL]

    //==연관관계 편의 메서드==//
    // 양방향 관계일 경우, 매번 business logic 에서 줄낭비 줄이기 위해
    // 얘네는 객체간의 탐색이 주 목적이다.
    // setXXX 경우, 지금 Order 가 주인이라 DB에 반영'은' 가능, 근데 addXX 메서드는 Order가 비주인이라 DB에 반영은 안된다.
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //==생성 메서드==//
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member); // 주문자 정보 세팅
        order.setDelivery(delivery);

        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDateTime(LocalDateTime.now());

        return order;
    }

    //==비즈니스 로직==//
    //주문 취소
    public void cancel() {
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송 완료된 상품입니다.");
        }
        setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    public int getTotalPrice() {
        return orderItems.stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum();
    }
}
