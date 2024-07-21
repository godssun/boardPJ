package com.github.boardpj.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Component
public class JwtUtil {

	private String SECRET_KEY;

	@Value("${jwt.secret-key-source}")
	private String secretKeyEnv;

	@PostConstruct
	public void init() {
		this.SECRET_KEY = Base64.getEncoder().encodeToString(secretKeyEnv.getBytes());
	}

	/**
	 * 사용자 이메일을 기반으로 JWT 토큰을 생성합니다.
	 * @param email 사용자 이메일
	 * @return 생성된 JWT 토큰
	 */
	public String generateToken(String email) {
		Map<String, Object> claims = new HashMap<>(); // JWT의 클레임 설정 (필요시 추가 데이터 포함 가능)
		return Jwts.builder()
				.setClaims(claims) // 클레임 설정
				.setSubject(email) // 토큰의 주체 설정 (보통 사용자 이메일)
				.setIssuedAt(new Date(System.currentTimeMillis())) // 토큰 발행 시간 설정
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 토큰 만료 시간 설정 (10시간 유효)
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY) // 서명 알고리즘과 비밀 키 설정
				.compact(); // 토큰 생성
	}

	/**
	 * 주어진 JWT 토큰이 유효한지 검증합니다.
	 * @param token 검증할 JWT 토큰
	 * @param email 사용자 이메일
	 * @return 토큰이 유효한지 여부
	 */
	public Boolean validateToken(String token, String email) {
		final String extractedEmail = extractEmail(token); // 토큰에서 이메일 추출
		return (extractedEmail.equals(email) && !isTokenExpired(token)); // 이메일이 일치하고 토큰이 만료되지 않았는지 확인
	}

	/**
	 * JWT 토큰에서 이메일을 추출합니다.
	 * @param token JWT 토큰
	 * @return 추출된 이메일
	 */
	public String extractEmail(String token) {
		return extractClaim(token, Claims::getSubject); // 클레임에서 주체(subject)를 추출 (이메일)
	}

	/**
	 * JWT 토큰에서 특정 클레임을 추출합니다.
	 * @param token JWT 토큰
	 * @param claimsResolver 클레임 해석 함수
	 * @param <T> 클레임 타입
	 * @return 추출된 클레임
	 */
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token); // 모든 클레임을 추출
		return claimsResolver.apply(claims); // 클레임 해석 함수 적용
	}

	/**
	 * JWT 토큰에서 모든 클레임을 추출합니다.
	 * @param token JWT 토큰
	 * @return 추출된 클레임
	 */
	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody(); // 서명 키를 사용하여 토큰 파싱 및 클레임 추출
	}

	/**
	 * JWT 토큰이 만료되었는지 확인합니다.
	 * @param token JWT 토큰
	 * @return 토큰 만료 여부
	 */
	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date()); // 현재 시간과 만료 시간을 비교하여 만료 여부 확인
	}

	/**
	 * JWT 토큰에서 만료 시간을 추출합니다.
	 * @param token JWT 토큰
	 * @return 추출된 만료 시간
	 */
	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration); // 클레임에서 만료 시간 추출
	}
}
