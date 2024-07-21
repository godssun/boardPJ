package com.github.boardpj.web.dto.post;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@AllArgsConstructor
@Builder
@Setter
@NoArgsConstructor
@ToString
public class PostDTO {
    private Integer id;
    private String title;
    private String content;
    private String author;
    private LocalDateTime createdAt;

}