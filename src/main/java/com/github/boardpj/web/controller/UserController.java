package com.github.boardpj.web.controller;


import com.github.boardpj.service.UserService;
import com.github.boardpj.web.dto.singupandloginout.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

	@Autowired
	private UserService userService;

	/**
	 * 회원가입 엔드포인트
	 * @param request 회원가입 요청 정보 (이메일, 비밀번호)
	 * @return 회원가입 완료 메시지를 포함한 응답
	 */
	@PostMapping("/signup")
	public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest request) {
		userService.signup(request.getEmail(), request.getPassword()); // 회원가입 처리
		return ResponseEntity.ok(new SignupResponse("회원가입이 완료되었습니다.")); // 완료 메시지를 응답으로 반환
	}

	/**
	 * 로그인 엔드포인트
	 * @param request 로그인 요청 정보 (이메일, 비밀번호)
	 * @return 로그인 완료 메시지와 JWT 토큰을 포함한 응답
	 */
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
		String token = userService.login(request.getEmail(), request.getPassword()); // 로그인 처리 및 JWT 토큰 생성
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + token); // 응답 헤더에 토큰 추가
		return new ResponseEntity<>(new LoginResponse("로그인이 성공적으로 완료되었습니다."), headers, HttpStatus.OK); // 완료 메시지와 토큰을 응답으로 반환
	}

	/**
	 * 로그아웃 엔드포인트
	 * @param request 로그아웃 요청 정보 (이메일)
	 * @return 로그아웃 완료 메시지를 포함한 응답
	 */
	@PostMapping("/logout")
	public ResponseEntity<LogoutResponse> logout(@RequestBody LogoutRequest request) {
		userService.logout(request.getEmail()); // 로그아웃 처리
		return ResponseEntity.ok(new LogoutResponse("로그아웃이 완료되었습니다.")); // 완료 메시지를 응답으로 반환
	}
}
