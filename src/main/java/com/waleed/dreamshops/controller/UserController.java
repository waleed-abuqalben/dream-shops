package com.waleed.dreamshops.controller;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.waleed.dreamshops.dto.UserDto;
import com.waleed.dreamshops.exceptions.AlreadyExistsException;
import com.waleed.dreamshops.exceptions.ResourceNotFoundException;
import com.waleed.dreamshops.model.User;
import com.waleed.dreamshops.request.CreateUserRequest;
import com.waleed.dreamshops.request.UserUpdateRequest;
import com.waleed.dreamshops.response.ApiResponse;
import com.waleed.dreamshops.service.user.IUserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/users")
public class UserController {
	private final IUserService userService;
	
	@GetMapping("/{userId}/user")
	public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId) {
		try {
			User user =  userService.getUserById(userId);
			UserDto userDto = userService.convertUserToDto(user);
			return ResponseEntity.ok(new ApiResponse("success", userDto));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(NOT_FOUND)
					.body(new ApiResponse(e.getMessage(), null));
		}
	}
	
	@PostMapping("/add")
	public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest request) {
		try {
			User user = userService.createUser(request);
			UserDto userDto = userService.convertUserToDto(user);
			return ResponseEntity.ok(new ApiResponse("User Created", userDto));
		} catch (AlreadyExistsException e) {
			return ResponseEntity.status(CONFLICT)
					.body(new ApiResponse(e.getMessage(), null));
		}
	}
	
	@PutMapping("/{userId}/update")
	public ResponseEntity<ApiResponse> updateUser(
			  @RequestBody UserUpdateRequest request, 
			  @PathVariable Long userId) {
		try {
			User user = userService.updateUser(request, userId);
			UserDto userDto = userService.convertUserToDto(user);
			return ResponseEntity.ok(new ApiResponse("User Updated", userDto));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(NOT_FOUND)
					.body(new ApiResponse(e.getMessage(), null));
		}
	}
	
	@DeleteMapping("{userId}/delete")
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId) {
		try {
			userService.deleteUser(userId);
			return ResponseEntity.ok(new ApiResponse("User deleted", null));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(NOT_FOUND)
					.body(new ApiResponse(e.getMessage(), null));
		}
	}
	

}
