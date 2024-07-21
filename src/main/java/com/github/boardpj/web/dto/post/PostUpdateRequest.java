package com.github.boardpj.web.dto.post;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostUpdateRequest {
	private String title;
	private String content;
}
