package ma.alten.ecommerce.services.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import ma.alten.ecommerce.dtos.ProductDto;
import ma.alten.ecommerce.entities.Product;
import ma.alten.ecommerce.entities.enums.InventoryStatusEnum;
import ma.alten.ecommerce.exceptions.ProductNotFoundException;
import ma.alten.ecommerce.repositories.ProductRepository;
import ma.alten.ecommerce.utils.ProductMapper;

public class ProductServiceImplTest {

	@Mock
	private ProductRepository productRepository;

	@Mock
	private ProductMapper productMapper;

	@InjectMocks
	private ProductServiceImpl productService;

	private Product product;
	private ProductDto productDto;

	@BeforeEach
	public void beforeEach() {
		MockitoAnnotations.openMocks(this);
		product = initProduct();
		productDto = initProductDto();
	}

	private ProductDto initProductDto() {
		return new ProductDto(1L, "PRD001", "Test Product", "product description test",
				"http://ecommerce.alten.ma/productdemo.jpg", "Electronics", 99.99, 100, "REF_98", 10,
				InventoryStatusEnum.IN_STOCK.name(), 4.5, LocalDateTime.now().minusDays(5), LocalDateTime.now());
	}

	private Product initProduct() {
		Product product = new Product();
		product.setId(1L);
		product.setCode("PRD001");
		product.setName("Test Product");
		product.setDescription("product description test");
		product.setImage("http://ecommerce.alten.ma/productdemo.jpg");
		product.setCategory("Electronics");
		product.setPrice(199.99);
		product.setQuantity(100);
		product.setInternalReference("REF_98");
		product.setShellId(10);
		product.setInventoryStatus("InventoryStatusEnum.IN_STOCK.name()");
		product.setRating(4.5);
		product.setCreatedAt(LocalDateTime.now().minusDays(5));
		product.setUpdatedAt(LocalDateTime.now());

		return product;
	}

	@Test
	void testSaveProduct() {
	   when(productMapper.toEntity(any(ProductDto.class))).thenReturn(product);

	   when(productRepository.save(any(Product.class))).thenReturn(product);

	   productService.save(productDto);

	   verify(productMapper, times(1)).toEntity(productDto);

	   verify(productRepository, times(1)).save(product);

	   assertDoesNotThrow(() -> productService.save(productDto));
	}

	@Test
	void testGetAllProducts() {
		List<Product> productList = Arrays.asList(product);
		Page<Product> productPage = new PageImpl<>(productList);

		when(productRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(productPage);
		when(productMapper.toDto(product)).thenReturn(productDto);

		Page<ProductDto> result = productService.getAll(productDto, 0, 10);

		verify(productRepository, times(1)).findAll(any(Specification.class), eq(PageRequest.of(0, 10)));

		verify(productMapper, times(1)).toDto(product);

		assertNotNull(result);
		assertEquals(1, result.getTotalElements());
		assertEquals("Test Product", result.getContent().get(0).name());
	}

	@Test
    void testGetById_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toDto(product)).thenReturn(productDto);

        ProductDto result = productService.getById(1L);

        verify(productRepository, times(1)).findById(1L);
        verify(productMapper, times(1)).toDto(product);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Test Product", result.name());
    }

	@Test
    void testGetById_ProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getById(1L));
        verify(productRepository, times(1)).findById(1L);
    }

	@Test
    void testDeleteById_Success() {
        when(productRepository.existsById(1L)).thenReturn(true);

        productService.deleteById(1L);

        verify(productRepository, times(1)).existsById(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

	@Test
    void testDeleteById_ProductNotFound() {
        when(productRepository.existsById(1L)).thenReturn(false);

        assertThrows(ProductNotFoundException.class, () -> productService.deleteById(1L));

        verify(productRepository, times(1)).existsById(1L);
        verify(productRepository, times(0)).deleteById(anyLong());
    }

	@Test
    void testUpdate_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toDto(product)).thenReturn(productDto);

        ProductDto result = productService.update(productDto);

        verify(productRepository, times(1)).findById(1L);

        verify(productMapper, times(1)).toDto(product);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Test Product", result.name());
    }

	@Test
    void testUpdate_ProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.update(productDto));

        verify(productRepository, times(1)).findById(1L);
    }

}
