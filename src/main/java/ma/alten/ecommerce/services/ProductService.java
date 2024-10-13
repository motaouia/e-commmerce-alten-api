package ma.alten.ecommerce.services;

import org.springframework.data.domain.Page;

import ma.alten.ecommerce.dtos.ProductDto;

public interface ProductService {

	void save(ProductDto product);

	Page<ProductDto> getAll(ProductDto product, int page, int size);

	ProductDto getById(Long id);

	void deleteById(Long id);

	ProductDto update(ProductDto updatedProduct);

}
