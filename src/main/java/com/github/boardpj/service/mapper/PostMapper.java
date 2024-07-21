package com.github.boardpj.service.mapper;

import com.github.boardpj.repository.post.PostEntity;
import com.github.boardpj.web.dto.post.PostCreateRequest;
import com.github.boardpj.web.dto.post.PostDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PostMapper {
	PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

	@Mapping(target = "id", source = "postId")
	@Mapping(target = "title", source = "title")
	@Mapping(target = "content", source = "content")
	@Mapping(target = "author", source = "author")
	@Mapping(target = "createdAt", source = "createdAt")
	PostDTO toDto(PostEntity postEntity);

	@Mapping(target = "postId", source = "id")
	@Mapping(target = "title", source = "title")
	@Mapping(target = "content", source = "content")
	@Mapping(target = "author", source = "author")
	@Mapping(target = "createdAt", source = "createdAt")
	PostEntity toEntity(PostDTO postDTO);

	@Mapping(target = "postId", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "userId", ignore = true)
	@Mapping(source = "title", target = "title")
	@Mapping(source = "content", target = "content")
	@Mapping(source = "author", target = "author")
	PostEntity toEntity(PostCreateRequest postCreateRequest);

}
