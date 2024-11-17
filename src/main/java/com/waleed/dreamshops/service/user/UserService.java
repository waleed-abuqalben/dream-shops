package com.waleed.dreamshops.service.user;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.waleed.dreamshops.dto.UserDto;
import com.waleed.dreamshops.exceptions.AlreadyExistsException;
import com.waleed.dreamshops.exceptions.ResourceNotFoundException;
import com.waleed.dreamshops.model.User;
import com.waleed.dreamshops.repository.UserRepository;
import com.waleed.dreamshops.request.CreateUserRequest;
import com.waleed.dreamshops.request.UserUpdateRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
	private final UserRepository userRepository;
	private final ModelMapper modelMapper;
	private final PasswordEncoder passwordEncoder;

	@Override
	public User getUserById(Long userId) {
		return userRepository.findById(userId)
				.orElseThrow( () -> new ResourceNotFoundException(
							"User not found. id: "+userId));
	}

	@Override
	public User createUser(CreateUserRequest request) {
		return Optional.of(request)
				.filter(user -> !userRepository.existsByEmail(request.getEmail()))
				.map(req -> {
					User user = new User();
					user.setEmail(req.getEmail());
					user.setPassword(passwordEncoder.encode(req.getPassword()));
					user.setFirstName(req.getFirstName());
					user.setLastName(req.getLastName());
					return userRepository.save(user);
				}).orElseThrow(() -> 
					new AlreadyExistsException("Oops! "+request.getEmail()+" already used"));
	}

	@Override
	public User updateUser(UserUpdateRequest request, Long userId) {
		return userRepository.findById(userId).map(existingUser -> {
			existingUser.setFirstName(request.getFirstName());
			existingUser.setLastName(request.getLastName());
			return userRepository.save(existingUser);
		}).orElseThrow( () -> new ResourceNotFoundException(
				"User not found. id: "+userId));
	}

	@Override
	public void deleteUser(Long userId) {
		userRepository.findById(userId)
			.ifPresentOrElse(userRepository :: delete,
				() -> {throw new ResourceNotFoundException("User not found. id: "+userId);});
	}
	
	@Override
	public UserDto convertUserToDto(User user) {
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public User getAuthenticatedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		return userRepository.findByEmail(email);
	}
	

}
