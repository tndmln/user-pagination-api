package com.porto.pagination.controllers;

import com.porto.pagination.dto.PaginationResponse;
import com.porto.pagination.models.User;
import com.porto.pagination.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Tag(name = "Users API", description = "Endpoints for user pagination and search")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@Operation(summary = "Get paginated users")
	@GetMapping("/users")
	public ResponseEntity<PaginationResponse<User>> getUsers(@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size) {

		var result = userService.getUsers(page, size);
		return ResponseEntity.ok(result);
	}

	@Operation(summary = "Search users by name")
	@GetMapping("/users/search")
	public ResponseEntity<PaginationResponse<User>> searchUsers(@RequestParam String name,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {

		var result = userService.searchUsersByName(name, page, size);
		return ResponseEntity.ok(result);
	}
}
