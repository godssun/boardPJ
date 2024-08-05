package com.github.boardpj.web.controller;

import com.github.boardpj.service.PostService;
import com.github.boardpj.web.dto.post.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Post Controller", description = "게시물 관련 API")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class PostController {

	private static final Logger logger = LoggerFactory.getLogger(PostController.class);
	private final PostService postService;

	/**
	 * 모든 게시물을 조회하는 엔드포인트
	 * @return 게시물 리스트를 포함한 응답
	 */
	@GetMapping("/posts")
	@Operation(summary = "모든 게시물 조회", description = "모든 게시물을 조회합니다.")
	public ResponseEntity<PostsResponse> getAllPosts() {
		List<PostDTO> postDTOs = postService.getAllPosts(); // 모든 게시물을 조회
		logger.info("Returning posts: {}", postDTOs); // 조회된 게시물 리스트를 로그로 출력
		return ResponseEntity.ok(new PostsResponse(postDTOs)); // 게시물 리스트를 응답으로 반환
	}

	/**
	 * 작성자의 이메일을 통해 게시물을 검색하는 엔드포인트
	 * @param authorEmail 작성자 이메일
	 * @return 작성자의 게시물 리스트를 포함한 응답
	 */
	@GetMapping("/posts/search")
	@Operation(summary = "작성자 이메일로 게시물 검색", description = "작성자의 이메일을 통해 게시물을 검색합니다.")
	public ResponseEntity<PostsResponse> getPostsByAuthorEmail(@RequestParam String authorEmail) {
		List<PostDTO> posts = postService.getPostsByAuthorEmail(authorEmail); // 작성자의 이메일로 게시물을 조회
		return ResponseEntity.ok(new PostsResponse(posts)); // 조회된 게시물 리스트를 응답으로 반환
	}

	/**
	 * 새로운 게시물을 생성하는 엔드포인트
	 * @param postCreateRequest 생성할 게시물 정보
	 * @return 게시물 생성 결과 메시지를 포함한 응답
	 */
	@PostMapping("/posts")
	@Operation(summary = "새로운 게시물 생성", description = "새로운 게시물을 생성합니다.")
	public ResponseEntity<PostCreateResponse> createPost(@RequestBody PostCreateRequest postCreateRequest) {
		PostCreateResponse response = postService.createPost(postCreateRequest); // 새로운 게시물을 생성
		return ResponseEntity.ok(response); // 생성 결과 메시지를 응답으로 반환
	}

	/**
	 * 기존 게시물을 수정하는 엔드포인트
	 * @param postId 수정할 게시물 ID
	 * @param postUpdateRequest 수정할 게시물 정보
	 * @return 게시물 수정 결과 메시지를 포함한 응답
	 */
	@PutMapping("/posts/{postId}")
	@Operation(summary = "기존 게시물 수정", description = "기존 게시물을 수정합니다.")
	public ResponseEntity<PostUpdateResponse> updatePost(@PathVariable Integer postId, @RequestBody PostUpdateRequest postUpdateRequest) {
		PostUpdateResponse response = postService.updatePost(postId, postUpdateRequest); // 기존 게시물을 수정
		return ResponseEntity.ok(response); // 수정 결과 메시지를 응답으로 반환
	}
}