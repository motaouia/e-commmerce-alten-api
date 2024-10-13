package ma.alten.ecommerce.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import ma.alten.ecommerce.dtos.ProductDto;
import ma.alten.ecommerce.entities.Product;
import ma.alten.ecommerce.exceptions.ProductNotFoundException;
import ma.alten.ecommerce.repositories.ProductRepository;
import ma.alten.ecommerce.services.ProductService;
import ma.alten.ecommerce.utils.ProductMapper;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;
	private final ProductMapper productMapper;

	@Override
	public void save(ProductDto productDto) {
		Product product = productMapper.toEntity(productDto);
		productRepository.save(product);

	}

	public Page<ProductDto> getAll(ProductDto productDto, int page, int size) {
		Page<Product> products = productRepository.findAll(getSpecification(productDto), PageRequest.of(page, size));
		return products.map(productMapper::toDto);
	}

	private Specification<Product> getSpecification(ProductDto productDto) {
	    return (root, query, criteriaBuilder) -> {
	        List<Predicate> predicates = new ArrayList<>();

	        if (productDto.code() != null && !productDto.code().isEmpty()) {
	            predicates.add(criteriaBuilder.equal(root.get("code"), productDto.code()));
	        }
	        if (productDto.name() != null && !productDto.name().isEmpty()) {
	            predicates.add(criteriaBuilder.like(root.get("name"), "%" + productDto.name() + "%"));
	        }
	        if (productDto.category() != null && !productDto.category().isEmpty()) {
	            predicates.add(criteriaBuilder.equal(root.get("category"), productDto.category()));
	        }
	        if (productDto.price() != null) {
	            predicates.add(criteriaBuilder.equal(root.get("price"), productDto.price()));
	        }
	        if (productDto.inventoryStatus() != null && !productDto.inventoryStatus().isEmpty()) {
	            predicates.add(criteriaBuilder.equal(root.get("inventoryStatus"), productDto.inventoryStatus()));
	        }
	        if (productDto.rating() != null) {
	            predicates.add(criteriaBuilder.equal(root.get("rating"), productDto.rating()));
	        }
	        if (productDto.createdAt() != null) {
	            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), productDto.createdAt()));
	        }
	        if (productDto.updatedAt() != null) {
	            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("updatedAt"), productDto.updatedAt()));
	        }

	        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
	    };
	}


	@Override
	public ProductDto getById(Long id) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ProductNotFoundException("Product not found with id : " + id));
		return productMapper.toDto(product);
	}

	@Override
	public void deleteById(Long id) {
		if (!productRepository.existsById(id)) {
			throw new ProductNotFoundException("Product not found with id: " + id);
		}
		productRepository.deleteById(id);

	}

	@Override
	public ProductDto update(ProductDto updatedProduct) {
		Product existingProduct = productRepository.findById(updatedProduct.id()).orElseThrow(
				() -> new ProductNotFoundException("Product not found with id: " + updatedProduct.id()));
		return productMapper.toDto(existingProduct);

	}

}
