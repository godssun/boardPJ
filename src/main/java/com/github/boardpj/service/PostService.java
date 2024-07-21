package com.github.boardpj.service;

import com.github.boardpj.repository.post.PostEntity;
import com.github.boardpj.repository.post.PostJpaRepository;
import com.github.boardpj.repository.user.UserEntity;
import com.github.boardpj.repository.user.UserJpaRepository;
import com.github.boardpj.service.exceptions.NotFoundException;
import com.github.boardpj.service.mapper.PostMapper;
import com.github.boardpj.web.dto.post.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Slf4j
public class PostService {


	private final PostJpaRepository postJpaRepository;
	private final UserJpaRepository userJpaRepository;

	public List<PostDTO> getAllPosts() {
		return postJpaRepository.findAll().stream()
				.map(PostMapper.INSTANCE::toDto)
				.collect(Collectors.toList());
	}


	public List<PostDTO> getPostsByAuthorEmail(String authorEmail) {
		UserEntity user = userJpaRepository.findByEmail(authorEmail);
		List<PostEntity> posts = postJpaRepository.findByUserId(user.getId());

		return posts.stream()
				.map(PostMapper.INSTANCE::toDto)
				.collect(Collectors.toList());
	}

	public PostCreateResponse createPost(PostCreateRequest postCreateRequest) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userEmail = null;

		if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
			userEmail = ((UserDetails) authentication.getPrincipal()).getUsername();
		} else if (authentication != null) {
			userEmail = (String) authentication.getPrincipal();
		}

		if (userEmail == null) {
			throw new IllegalStateException("로그인된 사용자를 찾을 수 없습니다.");
		}

		UserEntity userEntity = userJpaRepository.findByEmail(userEmail);
		if (userEntity == null) {
			throw new IllegalStateException("사용자를 찾을 수 없습니다.");
		}

		PostEntity postEntity = PostMapper.INSTANCE.toEntity(postCreateRequest);
		postEntity.setUserId(userEntity.getId()); // 로그인된 사용자의 user_id 설정
		postJpaRepository.save(postEntity);
		return new PostCreateResponse("게시물이 성공적으로 작성되었습니다.");
	}

	public PostUpdateResponse updatePost(Integer postId, PostUpdateRequest postUpdateRequest) {
		PostEntity postEntity = postJpaRepository.findById(postId)
				.orElseThrow(() -> new NotFoundException("게시물을 찾을 수 없습니다."));

		postEntity.setTitle(postUpdateRequest.getTitle());
		postEntity.setContent(postUpdateRequest.getContent());
		postJpaRepository.save(postEntity);

		return new PostUpdateResponse("게시물이 성공적으로 수정되었습니다.");
	}
}
