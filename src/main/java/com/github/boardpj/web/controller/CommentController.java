package com.github.boardpj.web.controller;

import com.github.boardpj.service.CommentService;
import com.github.boardpj.web.dto.commnet.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Comment Controller", description = "댓글 관련 API")
@RequiredArgsConstructor
@Slf4j
public class CommentController {

	private final CommentService commentService;

	/**
	 * 모든 댓글을 조회하는 엔드포인트
	 * @return 댓글 리스트를 포함한 응답
	 */
	@GetMapping("/comments")
	@Operation(summary = "모든 댓글 조회", description = "모든 댓글을 조회합니다.")
	public ResponseEntity<CommentsResponse> getAllComments() {
		List<CommentDTO> commentDTOs = commentService.getAllComments();
		return ResponseEntity.ok(new CommentsResponse(commentDTOs));
	}

	/**
	 * 게시물 ID를 통해 댓글을 조회하는 엔드포인트
	 * @param postId 게시물 ID
	 * @return 게시물의 댓글 리스트를 포함한 응답
	 */
	@GetMapping("/comments/search")
	@Operation(summary = "게시물 ID로 댓글 조회", description = "게시물 ID를 통해 댓글을 조회합니다.")
	public ResponseEntity<CommentsResponse> getCommentsByPostId(@RequestParam Integer postId) {
		List<CommentDTO> comments = commentService.getCommentsByPostId(postId);
		return ResponseEntity.ok(new CommentsResponse(comments));
	}

	/**
	 * 새로운 댓글을 작성하는 엔드포인트
	 * @param commentRequest 생성할 댓글 정보
	 * @return 댓글 생성 결과 메시지를 포함한 응답
	 */
	@PostMapping("/comments")
	@Operation(summary = "새로운 댓글 작성", description = "새로운 댓글을 작성합니다.")
	public ResponseEntity<CommentResponse> addComment(@RequestBody CommentRequest commentRequest) {
		commentService.createComment(commentRequest);
		return ResponseEntity.ok(new CommentResponse("댓글이 성공적으로 작성되었습니다."));
	}

	/**
	 * 기존 댓글을 수정하는 엔드포인트
	 * @param commentId 수정할 댓글 ID
	 * @param commentUpdateRequest 수정할 댓글 정보
	 * @return 댓글 수정 결과 메시지를 포함한 응답
	 */
	@PutMapping("/comments/{commentId}")
	@Operation(summary = "기존 댓글 수정", description = "기존 댓글을 수정합니다.")
	public ResponseEntity<CommentUpdateResponse> updateComment(@PathVariable Integer commentId, @RequestBody CommentUpdateRequest commentUpdateRequest) {
		CommentUpdateResponse response = commentService.updateComment(commentId, commentUpdateRequest);
		return ResponseEntity.ok(response);
	}
}