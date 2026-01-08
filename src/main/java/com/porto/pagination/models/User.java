package com.porto.pagination.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Represents a user record returned by the external API")
public class User {

	@Schema(description = "User identifier", example = "1")
	private Long id;

	@JsonProperty("firstName")
	@Schema(description = "User's first name", example = "Tandi")
	private String firstName;

	@JsonProperty("lastName")
	@Schema(description = "User's last name", example = "Maulana")
	private String lastName;

	@Schema(description = "User age", example = "24")
	private Integer age;

	@Schema(description = "User email address", example = "tandi.maulan@example.com")
	private String email;
}
