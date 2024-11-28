package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.CommentDto;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "created", expression = "java(java.time.LocalDateTime.now())")
    Comment toComment(CommentDto commentDto);

    @Mapping(source = "author.name", target = "authorName")
    CommentDto toCommentDto(Comment comment);

}
