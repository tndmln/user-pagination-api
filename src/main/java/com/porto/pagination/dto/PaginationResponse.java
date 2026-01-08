package com.porto.pagination.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Paginated response wrapper")
public class PaginationResponse<T> {

	@Schema(description = "Current page number", example = "1")
	private int page;

	@Schema(description = "Number of items per page", example = "10")
	private int size;

	@Schema(description = "Total number of items available", example = "100")
	private long totalItems;

	@Schema(description = "Total number of pages available", example = "10")
	private int totalPages;

	@Schema(description = "List of items for the current page")
	private List<T> data;
}