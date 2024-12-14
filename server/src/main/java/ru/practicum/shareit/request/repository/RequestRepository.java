package ru.practicum.shareit.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface RequestRepository extends JpaRepository<ItemRequest, Integer> {
    List<ItemRequest> findAllByRequestorId(Integer ownerId);

    @Query("SELECT r FROM ItemRequest AS r JOIN r.requestor AS u WHERE u.id != :ownerId")
    List<ItemRequest> getAllRequestsOfOtherUsers(@Param("ownerId") Integer ownerId);


}
