package com.porto.pagination.services;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.porto.pagination.dto.PaginationResponse;
import com.porto.pagination.exceptions.BadRequestException;
import com.porto.pagination.exceptions.ExternalApiException;
import com.porto.pagination.models.ExternalApiResponse;
import com.porto.pagination.models.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {

	
	@Value("${external.api.url:https://dummyjson.com/users}")
	private String externalApiUrl;

	@Value("${app.cache.duration-ms:300000}")
	private long cacheDuration;

	private final RestTemplate restTemplate;

	private List<User> cachedUsers;
	private long lastFetchTime;

	public UserService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public PaginationResponse<User> getUsers(int page, int size) {
		validatePagination(page, size);

		var users = loadUsers();
		return paginate(users, page, size);
	}

	public PaginationResponse<User> searchUsersByName(String name, int page, int size) {
		validatePagination(page, size);

		var keyword = name.toLowerCase();
		var users = loadUsers();

		var result = users.stream().filter(u -> matchesName(u, keyword)).toList();

		return paginate(result, page, size);
	}

	// --- Internal Helpers --- //

	private Boolean matchesName(User u, String keyword) {
		return (u.getFirstName() != null && u.getFirstName().toLowerCase().contains(keyword))
				|| (u.getLastName() != null && u.getLastName().toLowerCase().contains(keyword));
	}

	private synchronized List<User> loadUsers() {
		var now = System.currentTimeMillis();

		if (cachedUsers != null && (now - lastFetchTime) < cacheDuration) {
			return cachedUsers;
		}

		try {
			log.info("Fetching users from API: {}", externalApiUrl);

			var response = restTemplate.getForObject(externalApiUrl, ExternalApiResponse.class);

			if (response == null || response.getUsers() == null || response.getUsers().isEmpty()) {
				throw new ExternalApiException("External API returned empty data");
			}

			cachedUsers = response.getUsers();
			lastFetchTime = now;

			return cachedUsers;

		} catch (HttpClientErrorException e) {
			throw new ExternalApiException("External API error: " + e.getStatusCode());
		} catch (ResourceAccessException e) {
			throw new ExternalApiException("Network error: " + e.getMessage());
		} catch (Exception e) {
			throw new ExternalApiException("Unexpected error: " + e.getMessage());
		}
	}

	private PaginationResponse<User> paginate(List<User> users, int page, int size) {
		if (users == null || users.isEmpty()) {
			return new PaginationResponse<>(page, size, 0, 0, Collections.emptyList());
		}

		var total = users.size();
		var totalPages = (int) Math.ceil((double) total / size);

		if (page > totalPages && totalPages > 0) {
			page = totalPages;
		}

		var from = (page - 1) * size;
		var to = Math.min(from + size, total);

		if (from >= total) {
			return new PaginationResponse<>(page, size, total, totalPages, Collections.emptyList());
		}

		var content = users.subList(from, to);
		return new PaginationResponse<>(page, size, total, totalPages, content);
	}

	private void validatePagination(int page, int size) {
		if (page <= 0) {
			throw new BadRequestException("Page must be greater than 0");
		}
		if (size <= 0) {
			throw new BadRequestException("Page must be greater than 0");
		}
		if (size > 100) {
			throw new BadRequestException("Size must not exceed 100");
		}
	}

}
