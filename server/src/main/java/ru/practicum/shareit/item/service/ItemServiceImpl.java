package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.InvalidRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.*;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemStorage;
    private final UserRepository userStorage;
    private final BookingRepository bookingRepository;
    private final CommentMapper commentMapper;
    private final ItemMapper itemMapper;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public ItemDto addItem(Integer ownerId, ItemDto itemDto) {
        validateItemDto(itemDto);
        userStorage.findById(ownerId).orElseThrow(() -> new NotFoundException("User " + ownerId + " is not found"));
        Item item = itemMapper.itemDtoToItem(itemDto);
        item.setOwnerId(ownerId);
        return itemMapper.itemToItemDto(itemStorage.save(item));
    }

    @Override
    @Transactional
    public ItemDto editItem(Integer ownerId, Integer itemId, ItemDto itemDto) {
        Item item = itemStorage.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item " + itemId + " is not found"));

        if (!item.getOwnerId().equals(ownerId))
            throw new AccessDeniedException(String.format("User %d has no access to modify the resource", ownerId));

        if (itemDto.getName() != null && !itemDto.getName().isBlank())
            item.setName(itemDto.getName());
        if (itemDto.getDescription() != null && !itemDto.getDescription().isBlank())
            item.setDescription(itemDto.getDescription());
        if (itemDto.getAvailable() != null)
            item.setAvailable(itemDto.getAvailable());

        return itemMapper.itemToItemDto(item);
    }

    @Override
    public ItemResponseOnlyDto getItemById(Integer userId, Integer itemId) {
        Item item = itemStorage.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item " + itemId + " is not found"));
        return prepareItemForResponseDto(userId, item);
    }

    @Override
    public List<ItemResponseOnlyDto> getAllItemsByOwner(Integer ownerId) {
        List<Item> items = itemStorage.findAllByOwnerId(ownerId);
        return items.stream()
                .map(item -> prepareItemForResponseDto(item.getOwnerId(), item))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text == null || text.isBlank())
            return Collections.emptyList();
        return itemStorage.findItemsBySearchQuery(text).stream()
                .map(itemMapper::itemToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto addComment(Integer authorId, Integer itemId, CommentDto commentDto) {
        if (commentDto.getText() == null || commentDto.getText().isBlank())
            throw new InvalidRequestException("empty comment");

        LocalDateTime currentTime = LocalDateTime.now();

        List<Booking> bookings = bookingRepository.findAllByItemIdAndBookerIdAndEndBefore(itemId, authorId, currentTime);

        if (bookings.isEmpty())
            throw new InvalidRequestException("User did not book the item or booking period have not finished");

        User author = userStorage.findById(authorId).orElseThrow(() -> new NotFoundException("user not found"));
        Item item = itemStorage.findById(itemId).orElseThrow(() -> new NotFoundException("item not found"));
        Comment comment = commentMapper.toComment(commentDto);
        comment.setItem(item);
        comment.setAuthor(author);

        return commentMapper.toCommentDto(commentRepository.save(comment));
    }

    private ItemResponseOnlyDto prepareItemForResponseDto(Integer userId, Item item) {
        ItemResponseOnlyDto itemResponseDto = itemMapper.itemToItemResponseDto(item);
        List<Booking> bookingsOfItem = bookingRepository.findAllByItemOwnerId(item.getOwnerId());

        if (item.getOwnerId().equals(userId)) {
            bookingsOfItem.stream()
                    .filter(b -> b.getStart().isAfter(LocalDateTime.now())
                            && b.getStatus().equals(BookingStatus.APPROVED))
                    .min(Comparator.comparing(Booking::getStart))
                    .ifPresent(booking -> itemResponseDto.setNextBooking(booking.getStart()));

            bookingsOfItem.stream()
                    .filter(b -> b.getEnd().isBefore(LocalDateTime.now())
                            && b.getStatus().equals(BookingStatus.APPROVED))
                    .max(Comparator.comparing(Booking::getEnd))
                    .ifPresent(booking -> itemResponseDto.setLastBooking(booking.getStart()));
        }

        List<CommentDto> commentsDto = commentRepository.findAllByItemId(item.getId())
                .stream().map(commentMapper::toCommentDto).toList();

        itemResponseDto.setComments(commentsDto);
        return itemResponseDto;
    }

    private void validateItemDto(ItemDto itemDto) {
        if (itemDto.getName() == null || itemDto.getName().isBlank())
            throw new ValidationException("name should not be empty");
        if (itemDto.getDescription() == null || itemDto.getDescription().isBlank())
            throw new ValidationException("Invalid Description: should not be empty");
        if (itemDto.getAvailable() == null)
            throw new ValidationException("Availability: should not be empty");
    }

}
