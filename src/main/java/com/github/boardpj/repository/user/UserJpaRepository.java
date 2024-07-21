package com.github.boardpj.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, Integer> {
	UserEntity findByEmailAndPassword(String email, String password);
	UserEntity findByEmail(String email);

}
