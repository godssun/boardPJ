package com.github.boardpj.web.dto.post;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
public class PostsResponse {
    private List<PostDTO> posts;
}