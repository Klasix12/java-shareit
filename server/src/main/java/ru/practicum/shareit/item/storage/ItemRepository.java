package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByOwnerId(Long id);

    @Query("select it " +
            "from Item as it " +
            "where it.available = true " +
            "and (lower(:text) like concat('%', lower(it.name), '%') " +
            "or lower(:text) like concat('%', lower(it.description), '%'))")
    List<Item> findAllByNameOrDescription(String text);

    List<Item> findAllByItemRequestId(Long id);

    @Query("select it " +
    "from Item as it " +
    "where it.itemRequest.id in (:ids)")
    List<Item> findAllByItemRequestIds(List<Long> ids);
}
