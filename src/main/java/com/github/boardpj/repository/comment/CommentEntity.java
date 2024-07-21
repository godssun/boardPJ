package com.github.boardpj.repository.comment;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comment_id")
	private Integer commentId;

	@Column(name = "content")
	private String content;

	@Column(name = "author")
	private String author;

	@Column(name = "post_id")
	private Integer postId;

	@Column(name = "user_id")
	private Integer userId;

	@Column(name = "created_at")
	private LocalDateTime createdAt;
}
