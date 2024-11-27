package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findAllByOwnerId(Integer ownerId);

    //@Query("SELECT i from Item i WHERE i.available = true AND (CONTAINS(lower(i.name), lower(:text)) OR CONTAINS(lower(i.description), lower(:text)))")
    @Query("SELECT i FROM Item i WHERE i.available = true AND (LOWER(i.name) LIKE LOWER(CONCAT('%', :text, '%')) OR LOWER(i.description) LIKE LOWER(CONCAT('%', :text, '%')))")
    List<Item> findItemsBySearchQuery(String text);


}
