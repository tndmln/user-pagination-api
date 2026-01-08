package com.porto.pagination.models;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Response from external API containing user data")
public class ExternalApiResponse {

	@Schema(description = "List of users")
	private List<User> users;

	@Schema(description = "Total number of users", example = "100")
	private Integer total;

	@Schema(description = "Number of records skipped", example = "0")
	private Integer skip;

	@Schema(description = "Number of records returned", example = "100")
	private Integer limit;
}