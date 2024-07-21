package com.github.boardpj.service;

import com.github.boardpj.repository.comment.CommentEntity;
import com.github.boardpj.repository.comment.CommentJpaRepository;
import com.github.boardpj.repository.user.UserEntity;
import com.github.boardpj.repository.user.UserJpaRepository;
import com.github.boardpj.service.exceptions.NotFoundException;
import com.github.boardpj.service.mapper.CommentMapper;
import com.github.boardpj.web.dto.commnet.CommentDTO;
import com.github.boardpj.web.dto.commnet.CommentRequest;
import com.github.boardpj.web.dto.commnet.CommentUpdateRequest;
import com.github.boardpj.web.dto.commnet.CommentUpdateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

	private final CommentJpaRepository commentJpaRepository;
	private final UserJpaRepository userJpaRepository;

	public List<CommentDTO> getAllComments() {
		return commentJpaRepository.findAll().stream()
				.map(CommentMapper.INSTANCE::toDto)
				.collect(Collectors.toList());
	}

	public List<CommentDTO> getCommentsByPostId(Integer postId) {
		return commentJpaRepository.findByPostId(postId).stream()
				.map(CommentMapper.INSTANCE::toDto)
				.collect(Collectors.toList());
	}

	public void createComment(CommentRequest commentRequest) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userEmail = null;

		if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
			userEmail = ((UserDetails) authentication.getPrincipal()).getUsername();
		} else if (authentication != null) {
			userEmail = (String) authentication.getPrincipal();
		}

		log.info("Authenticated user email: {}", userEmail);

		if (userEmail == null) {
			throw new IllegalStateException("로그인된 사용자를 찾을 수 없습니다.");
		}

		UserEntity userEntity = userJpaRepository.findByEmail(userEmail);
		if (userEntity == null) {
			throw new IllegalStateException("사용자를 찾을 수 없습니다.");
		}

		log.info("User entity found: {}", userEntity);

		CommentEntity commentEntity = CommentMapper.INSTANCE.toEntityCreate(commentRequest);
		commentEntity.setUserId(userEntity.getId()); // 로그인된 사용자의 user_id 설정
		commentEntity.setAuthor(commentRequest.getAuthor()); // 요청된 작성자를 author로 설정
		commentJpaRepository.save(commentEntity);

		log.info("Comment successfully created: {}", commentEntity);
	}

	public CommentUpdateResponse updateComment(Integer commentId, CommentUpdateRequest commentUpdateRequest) {
		CommentEntity commentEntity = commentJpaRepository.findById(commentId)
				.orElseThrow(() -> new NotFoundException("댓글을 찾을 수 없습니다."));

		commentEntity.setContent(commentUpdateRequest.getContent());
		commentJpaRepository.save(commentEntity);

		return new CommentUpdateResponse("댓글이 성공적으로 수정되었습니다.");
	}
}
