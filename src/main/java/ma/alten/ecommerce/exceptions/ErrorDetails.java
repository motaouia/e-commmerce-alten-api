package ma.alten.ecommerce.exceptions;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetails {
	private int statusCode;
	private LocalDateTime date;
	private String message;
	private String description;
}
