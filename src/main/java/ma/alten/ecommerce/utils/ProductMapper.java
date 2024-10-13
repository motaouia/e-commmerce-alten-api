package ma.alten.ecommerce.utils;

import org.springframework.stereotype.Component;

import ma.alten.ecommerce.dtos.ProductDto;
import ma.alten.ecommerce.entities.Product;

@Component
public class ProductMapper {

	 public Product toEntity(ProductDto productDto) {
	        return Product.builder()
	                .id(productDto.id())
	                .code(productDto.code())
	                .name(productDto.name())
	                .description(productDto.description())
	                .image(productDto.image())
	                .category(productDto.category())
	                .price(productDto.price())
	                .quantity(productDto.quantity())
	                .internalReference(productDto.internalReference())
	                .shellId(productDto.shellId())
	                .inventoryStatus(productDto.inventoryStatus())
	                .rating(productDto.rating())
	                .createdAt(productDto.createdAt())
	                .updatedAt(productDto.updatedAt())
	                .build();
	    }

	    public ProductDto toDto(Product product) {
	        return new ProductDto(
	                product.getId(),
	                product.getCode(),
	                product.getName(),
	                product.getDescription(),
	                product.getImage(),
	                product.getCategory(),
	                product.getPrice(),
	                product.getQuantity(),
	                product.getInternalReference(),
	                product.getShellId(),
	                product.getInventoryStatus(),
	                product.getRating(),
	                product.getCreatedAt(),
	                product.getUpdatedAt()
	        );
	    }

}
