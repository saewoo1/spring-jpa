package jpabook.jpashop.api.orderdto;

import lombok.Data;

@Data
public class OrderResult<T> {
    private int count;
    private T data;

    public OrderResult(int count, T data) {
        this.count = count;
        this.data = data;
    }
}
