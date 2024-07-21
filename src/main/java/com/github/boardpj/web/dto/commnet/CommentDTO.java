package com.github.boardpj.web.dto.commnet;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentDTO {
	private Integer id;
	private String content;
	private String author;
	private Integer postId;
	private LocalDateTime createdAt;
}