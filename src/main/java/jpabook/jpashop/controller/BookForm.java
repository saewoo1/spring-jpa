package jpabook.jpashop.controller;

import jpabook.jpashop.domain.item.Book;
import lombok.Data;

@Data
public class BookForm {

    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    private String author;
    private String isbn;

    public static BookForm createBookForm(Book book) {
        BookForm bookForm = new BookForm();

        bookForm.setName(book.getName());
        bookForm.setId(book.getId());
        bookForm.setIsbn(bookForm.getIsbn());
        bookForm.setPrice(book.getPrice());
        bookForm.setStockQuantity(book.getStockQuantity());
        bookForm.setAuthor(book.getAuthor());

        return bookForm;
    }
}
