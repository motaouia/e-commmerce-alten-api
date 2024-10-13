package ma.alten.ecommerce.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ProductExceptionHandler {

	@ExceptionHandler(ProductNotFoundException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public ErrorDetails productNotFoundException(ProductNotFoundException productNotFoundException,
			WebRequest webRequest) {

		ErrorDetails errorDetails = new ErrorDetails(HttpStatus.NOT_FOUND.value(), LocalDateTime.now(),
				productNotFoundException.getMessage(), webRequest.getDescription(false));
		return errorDetails;
	}
	
	@ExceptionHandler(InvalidProductException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public ErrorDetails invalidProductException(InvalidProductException invalidProductException,
			WebRequest webRequest) {

		ErrorDetails errorDetails = new ErrorDetails(HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(),
				invalidProductException.getMessage(), webRequest.getDescription(false));
		return errorDetails;
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorDetails globalExceptionHanlder(Exception exception, WebRequest webRequest) {
		ErrorDetails errorDetails = new ErrorDetails(HttpStatus.NOT_FOUND.value(), LocalDateTime.now(),
				exception.getMessage(), webRequest.getDescription(false));
		return errorDetails;
	}

}
