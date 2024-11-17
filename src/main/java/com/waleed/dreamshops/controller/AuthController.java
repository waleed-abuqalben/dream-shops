package com.waleed.dreamshops.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.waleed.dreamshops.request.LoginRequest;
import com.waleed.dreamshops.response.ApiResponse;
import com.waleed.dreamshops.response.JwtResponse;
import com.waleed.dreamshops.security.jwt.JwtUtils;
import com.waleed.dreamshops.security.user.ShopUserDetails;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/auth")
public class AuthController {
	private final AuthenticationManager authenticationManager;
	private final JwtUtils jwtUtils;
	
	@PostMapping("/login")
	public ResponseEntity<ApiResponse> login(
			@Valid @RequestBody LoginRequest request){
		
		try {
			Authentication authentication = authenticationManager
					.authenticate(
						new UsernamePasswordAuthenticationToken(
								request.getEmail(), request.getPassword()));
				SecurityContextHolder.getContext()
						.setAuthentication(authentication);
				String jwtToken = jwtUtils.generateTokenForUser(authentication);
				ShopUserDetails userDetails = (ShopUserDetails) authentication.getPrincipal();
				JwtResponse jwtResponse = new JwtResponse(userDetails.getId(), jwtToken);
				return ResponseEntity.ok(new ApiResponse("Login success!", jwtResponse));
			
		} catch (AuthenticationException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse(e.getMessage(), null));
		}
		
		
	}

}
