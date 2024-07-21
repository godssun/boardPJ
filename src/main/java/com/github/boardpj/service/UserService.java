package com.github.boardpj.service;


import com.github.boardpj.repository.user.UserEntity;
import com.github.boardpj.repository.user.UserJpaRepository;
import com.github.boardpj.service.exceptions.InvalidValueException;
import com.github.boardpj.service.exceptions.NotFoundException;
import com.github.boardpj.service.exceptions.PasswordMismatchException;
import com.github.boardpj.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {


	private final PasswordEncoder passwordEncoder;
	private final UserJpaRepository userJpaRepository;
	private final JwtUtil jwtUtil;

	public void signup(String email, String password) {
		if (email == null || password == null) {
			throw new InvalidValueException("이메일과 비밀번호는 필수입니다.");
		}
		UserEntity userEntity = new UserEntity();
		userEntity.setEmail(email);
		userEntity.setPassword(passwordEncoder.encode(password)); // 비밀번호 암호화
		userJpaRepository.save(userEntity); // 사용자 정보 저장
	}

	public String login(String email, String password) {
		UserEntity userEntity = userJpaRepository.findByEmail(email);
		if (userEntity == null) {
			throw new NotFoundException("사용자를 찾을 수 없습니다.");
		}
		if (!passwordEncoder.matches(password, userEntity.getPassword())) { // 암호화된 비밀번호 비교
			throw new PasswordMismatchException("비밀번호가 다릅니다.");
		}
			userEntity.setLoggedIn(true); //로그인 상태 설정
			String token = jwtUtil.generateToken(email); // JWT 토근 설정
			userEntity.setLoginToken(token); // 사용자 엔티티에 토큰 저장
			userJpaRepository.save(userEntity); // 업데이트된 사용자 정보 저장
			return token; // 토큰 반환

	}

	public void logout(String email) {
		UserEntity userEntity = userJpaRepository.findByEmail(email);
		if (userEntity == null) {
			throw new NotFoundException("사용자를 찾을 수 없습니다.");
		}
			userEntity.setLoggedIn(false); // 로그아웃 상태 설정
			userEntity.setLoginToken(null); // 토큰 제거
			userJpaRepository.save(userEntity); // 업데이트된 사용자 정보 저장

	}

	public boolean vaildateToken(String token, String email) {
		return jwtUtil.validateToken(token, email); // 토큰 유효성 검증
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity userEntity = userJpaRepository.findByEmail(email);
		if (userEntity == null) {
			throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
		}
		return new org.springframework.security.core.userdetails.User(userEntity.getEmail(), userEntity.getPassword(), new ArrayList<>());
	}
}
