package jpabook.jpashop.service;

import jpabook.jpashop.controller.BookForm;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.UpgradeItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final UpgradeItemRepository upgradeItemRepository;

    @Transactional
    public void saveItem(Item item) {
//        itemRepository.save(item);
        upgradeItemRepository.save(item);
    }

    @Transactional
    public void updateItem(Long itemId, BookForm bookForm) {
        // repository 에서 가져온 애 -> JPA 가 영속성으로 관리중.
        Item item = upgradeItemRepository.findById(itemId)
                .orElseThrow(IllegalArgumentException::new);
        item.changeInformation(bookForm.getName(), bookForm.getPrice(), bookForm.getStockQuantity());
//        item.setName(book.getName());
//        item.setPrice(book.getPrice());
//        item.setStockQuantity(book.getStockQuantity());
    }

    public List<Item> findItems() {
        return upgradeItemRepository.findAll();
    }

    public Item findOne(Long id) {
        return upgradeItemRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }
}
