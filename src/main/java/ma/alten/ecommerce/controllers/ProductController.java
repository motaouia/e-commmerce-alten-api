package ma.alten.ecommerce.controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import ma.alten.ecommerce.dtos.ProductDto;
import ma.alten.ecommerce.exceptions.InvalidProductException;
import ma.alten.ecommerce.services.ProductService;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;

	@PostMapping
	@Operation(summary = "Create a new product", description = "Saves a new product in the system")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Product created successfully", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content) })
	public ResponseEntity<String> saveProduct(@RequestBody ProductDto productDto) {
		productService.save(productDto);
		return ResponseEntity.status(HttpStatus.CREATED).body("Product saved successfully.");
	}

	@GetMapping
	@Operation(summary = "Get all products", description = "Retrieve a paginated list of all products with optional filtering")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successful retrieval of products", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
			@ApiResponse(responseCode = "400", description = "Invalid request parameters", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content) })
	public ResponseEntity<Page<ProductDto>> getAllProducts(ProductDto productDto,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

		Page<ProductDto> productDtos = productService.getAll(productDto, page, size);
		return ResponseEntity.ok(productDtos);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get a product by ID", description = "Retrieve a specific product by its unique ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Product found and returned successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDto.class))),
			@ApiResponse(responseCode = "404", description = "Product not found", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content) })
	public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
		if(id <= 0) {
			throw new InvalidProductException("Invalid Product ID: " +id);
		}
		ProductDto productDto = productService.getById(id);
		return ResponseEntity.ok(productDto);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete a product by ID", description = "Deletes a specific product by its unique ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Product deleted successfully", content = @Content),
			@ApiResponse(responseCode = "404", description = "Product not found", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content) })
	public ResponseEntity<HttpStatus> deleteProductById(@PathVariable Long id) {
		productService.deleteById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Update a product", description = "Updates an existing product with the provided details")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Product updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDto.class))),
			@ApiResponse(responseCode = "400", description = "Invalid input or ID mismatch", content = @Content),
			@ApiResponse(responseCode = "404", description = "Product not found", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content) })
	public ResponseEntity<ProductDto> putProduct(@PathVariable Long id, @RequestBody ProductDto updatedProduct) {
		return updateProduct(id, updatedProduct);
	}

	@PatchMapping("/{id}")
	@Operation(summary = "Partially update a product", description = "Updates an existing product with the provided details. Only specified fields will be updated.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Product updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDto.class))),
			@ApiResponse(responseCode = "400", description = "Invalid input or ID mismatch", content = @Content),
			@ApiResponse(responseCode = "404", description = "Product not found", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content) })
	public ResponseEntity<ProductDto> patchProduct(@PathVariable Long id, @RequestBody ProductDto updatedProduct) {
		return updateProduct(id, updatedProduct);
	}

	private ResponseEntity<ProductDto> updateProduct(Long id, ProductDto updatedProduct) {
		if (!id.equals(updatedProduct.id())) {
			return ResponseEntity.badRequest().build();
		}
		ProductDto updatedProductDto = productService.update(updatedProduct);
		return ResponseEntity.ok(updatedProductDto);
	}

}
