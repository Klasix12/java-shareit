package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> findById(Long id);

    List<Item> findAllByOwner(User user);

    @Query("select it " +
            "from Item as it " +
            "where it.available = true " +
            "and (lower(:text) like concat('%', lower(it.name), '%') " +
            "or lower(:text) like concat('%', lower(it.description), '%'))")
    List<Item> findAllByNameOrDescription(String text);
}
