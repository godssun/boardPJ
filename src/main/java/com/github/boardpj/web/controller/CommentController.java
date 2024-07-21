package com.github.boardpj.web.controller;

import com.github.boardpj.service.CommentService;
import com.github.boardpj.web.dto.commnet.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class CommentController {


	private final CommentService commentService;

	@GetMapping("/comments")
	public ResponseEntity<CommentsResponse> getAllComments() {
		List<CommentDTO> commentDTOs = commentService.getAllComments();
		return ResponseEntity.ok(new CommentsResponse(commentDTOs));
	}

	@GetMapping("/comments/search")
	public ResponseEntity<CommentsResponse> getCommentsByPostId(@RequestParam Integer postId) {
		List<CommentDTO> comments = commentService.getCommentsByPostId(postId);
		return ResponseEntity.ok(new CommentsResponse(comments));
	}

	@PostMapping("/comments")
	public ResponseEntity<CommentResponse> addComment(@RequestBody CommentRequest commentRequest) {
		commentService.createComment(commentRequest);
		return ResponseEntity.ok(new CommentResponse("댓글이 성공적으로 작성되었습니다."));
	}

	@PutMapping("/comments/{commentId}")
	public ResponseEntity<CommentUpdateResponse> updateComment(@PathVariable Integer commentId, @RequestBody CommentUpdateRequest commentUpdateRequest) {
		CommentUpdateResponse response = commentService.updateComment(commentId, commentUpdateRequest);
		return ResponseEntity.ok(response);
	}
}
