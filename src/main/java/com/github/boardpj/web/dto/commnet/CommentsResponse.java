package com.github.boardpj.web.dto.commnet;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CommentsResponse {
	private List<CommentDTO> comments;

}
