package ma.alten.ecommerce.dtos;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

public record ProductDto(Long id, String code, String name, String description, String image, String category,
		Double price, Integer quantity, String internalReference, Integer shellId, String inventoryStatus,
		Double rating, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAt,
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime updatedAt) {
}
