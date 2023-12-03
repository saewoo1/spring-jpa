package jpabook.jpashop.service;

import java.util.List;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.UpgradeItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<Item> findItems() {
        return upgradeItemRepository.findAll();
    }

    public Item findOne(Long id) {
        return upgradeItemRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }
}
