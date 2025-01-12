package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private Long itemId = 0L;
    private final Map<Long, Map<Long, Item>> items = new LinkedHashMap<>();

    @Override
    public Item addItem(Item item) {
        Long userId = item.getOwner().getId();
        item.setId(++itemId);
        items.computeIfAbsent(userId, l -> new HashMap<>()).put(item.getId(), item);
        return item;
    }

    @Override
    public Item updateItem(Item newItem) {
        items.get(newItem.getOwner().getId()).put(newItem.getId(), newItem);
        return newItem;
    }

    @Override
    public Optional<Item> getItem(Long itemId) {
        for (Map<Long, Item> itemMap : items.values()) {
            Item item = itemMap.get(itemId);
            if (item != null) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Item> getItems(Long userId) {
        return items.get(userId).values().stream().toList();
    }

    @Override
    public List<Item> searchItems(String text) {
        List<Item> result = new ArrayList<>();
        for (Map<Long, Item> itemMap : items.values()) {
            for (Item item : itemMap.values()) {
                if (item.getAvailable() && (item.getName().toLowerCase().contains(text.toLowerCase()) ||
                        item.getDescription().toLowerCase().contains(text.toLowerCase()))) {
                    result.add(item);
                }
            }
        }
        return result;
    }
}
