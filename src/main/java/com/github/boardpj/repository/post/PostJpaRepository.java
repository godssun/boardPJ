package com.github.boardpj.repository.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostJpaRepository extends JpaRepository<PostEntity, Integer> {
	List<PostEntity> findByUserId(Integer userId);
}
