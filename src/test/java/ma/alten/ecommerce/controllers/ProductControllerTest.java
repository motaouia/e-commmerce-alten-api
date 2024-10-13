package ma.alten.ecommerce.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import ma.alten.ecommerce.dtos.ProductDto;
import ma.alten.ecommerce.entities.enums.InventoryStatusEnum;
import ma.alten.ecommerce.services.impl.ProductServiceImpl;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private ProductServiceImpl productServiceImpl;

	@InjectMocks
	private ProductController productController;

	ProductDto productDto;

	@BeforeEach
	public void beforeEach() {
		productDto = initProductDto();
	}

	private ProductDto initProductDto() {
		return new ProductDto(1L, "PRD001", "Test Product", "product description test",
				"http://ecommerce.alten.ma/productdemo.jpg", "Electronics", 99.99, 100, "REF_98", 10,
				InventoryStatusEnum.IN_STOCK.name(), 4.5, LocalDateTime.now().minusDays(5), LocalDateTime.now());
	}

	@Test
	public void testGetProductById() throws Exception {
		when(productServiceImpl.getById(1L)).thenReturn(productDto);

		mockMvc.perform(get("/api/v1/products/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.name").value("Test Product"))
				.andExpect(jsonPath("$.description").value("product description test"))
				.andExpect(jsonPath("$.image").value("http://ecommerce.alten.ma/productdemo.jpg"))
				.andExpect(jsonPath("$.category").value("Electronics")).andExpect(jsonPath("$.price").value(99.99))
				.andExpect(jsonPath("$.quantity").value(100)).andExpect(jsonPath("$.internalReference").value("REF_98"))
				.andExpect(jsonPath("$.shellId").value(10)).andExpect(jsonPath("$.inventoryStatus").value("IN_STOCK"))
				.andExpect(jsonPath("$.rating").value(4.5)).andExpect(jsonPath("$.createdAt").exists())
				.andExpect(jsonPath("$.updatedAt").exists());

		verify(productServiceImpl, times(1)).getById(1L);
	}

	@Test
	public void testGetAllProducts() throws Exception {

		ProductDto productDto2 = new ProductDto(2L, "PRD002", "Product 2", "Description 2",
				"http://example.com/product2.jpg", "Home Appliances", 199.99, 50, "TEST_REF_2", 20,
				InventoryStatusEnum.OUT_OF_STOCK.name(), 4.0, LocalDateTime.now().minusDays(10), LocalDateTime.now());

		ProductDto productDto3 = new ProductDto(3L, "PRD003", "Product 3", "Description 3",
				"http://example.com/product3.jpg", "Electronics", 99.99, 300, "TEST_REF_3", 30,
				InventoryStatusEnum.IN_STOCK.name(), 4.5, LocalDateTime.now().minusDays(5), LocalDateTime.now());

		Pageable pageable = PageRequest.of(0, 10);
		Page<ProductDto> productPage = new PageImpl<>(Arrays.asList(productDto, productDto2, productDto3), pageable, 2);

		when(productServiceImpl.getAll(any(ProductDto.class), eq(0), eq(10))).thenReturn(productPage);

		mockMvc.perform(
				get("/api/v1/products").param("page", "0").param("size", "10").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.content[0].id").value(1))
				.andExpect(jsonPath("$.content[0].name").value("Test Product"))
				.andExpect(jsonPath("$.content[0].description").value("product description test"))
				.andExpect(jsonPath("$.content[0].image").value("http://ecommerce.alten.ma/productdemo.jpg"))
				.andExpect(jsonPath("$.content[0].category").value("Electronics"))
				.andExpect(jsonPath("$.content[0].price").value(99.99))
				.andExpect(jsonPath("$.content[0].quantity").value(100))
				.andExpect(jsonPath("$.content[0].internalReference").value("REF_98"))
				.andExpect(jsonPath("$.content[0].shellId").value(10))
				.andExpect(jsonPath("$.content[0].inventoryStatus").value("IN_STOCK"))
				.andExpect(jsonPath("$.content[0].rating").value(4.5))
				.andExpect(jsonPath("$.content[0].createdAt").exists())
				.andExpect(jsonPath("$.content[0].updatedAt").exists()).andExpect(jsonPath("$.content[1].id").value(2))
				.andExpect(jsonPath("$.content[1].name").value("Product 2"))
				.andExpect(jsonPath("$.content[1].description").value("Description 2"))
				.andExpect(jsonPath("$.content[1].image").value("http://example.com/product2.jpg"))
				.andExpect(jsonPath("$.content[1].category").value("Home Appliances"))
				.andExpect(jsonPath("$.content[1].price").value(199.99))
				.andExpect(jsonPath("$.content[1].quantity").value(50))
				.andExpect(jsonPath("$.content[1].internalReference").value("TEST_REF_2"))
				.andExpect(jsonPath("$.content[1].shellId").value(20))
				.andExpect(jsonPath("$.content[1].inventoryStatus").value("OUT_OF_STOCK"))
				.andExpect(jsonPath("$.content[1].rating").value(4.0))
				.andExpect(jsonPath("$.content[1].createdAt").exists())
				.andExpect(jsonPath("$.content[1].updatedAt").exists());

		verify(productServiceImpl, times(1)).getAll(any(ProductDto.class), eq(0), eq(10));
	}

	@Test
	public void testSaveProduct() throws Exception {
		doNothing().when(productServiceImpl).save(any(ProductDto.class));

		String productJson = objectMapper.writeValueAsString(productDto);

		mockMvc.perform(post("/api/v1/products").contentType(MediaType.APPLICATION_JSON).content(productJson))
				.andExpect(status().isCreated());

		verify(productServiceImpl, times(1)).save(any(ProductDto.class));
	}

	@Test
	public void testDeleteProductById() throws Exception {
		Long productId = 1L;

		doNothing().when(productServiceImpl).deleteById(productId);

		mockMvc.perform(delete("/api/v1/products/{id}", productId).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());

		verify(productServiceImpl, times(1)).deleteById(productId);
	}

	@Test
	public void testPutProduct() throws Exception {
		Long productId = 1L;

		when(productServiceImpl.update(any(ProductDto.class))).thenReturn(productDto);

		String productJson = objectMapper.writeValueAsString(productDto);

		mockMvc.perform(
				put("/api/v1/products/{id}", productId).contentType(MediaType.APPLICATION_JSON).content(productJson))
				.andExpect(jsonPath("$.name").value("Test Product"))
				.andExpect(jsonPath("$.description").value("product description test"))
				.andExpect(jsonPath("$.image").value("http://ecommerce.alten.ma/productdemo.jpg"))
				.andExpect(jsonPath("$.category").value("Electronics")).andExpect(jsonPath("$.price").value(99.99))
				.andExpect(jsonPath("$.quantity").value(100)).andExpect(jsonPath("$.internalReference").value("REF_98"))
				.andExpect(jsonPath("$.shellId").value(10)).andExpect(jsonPath("$.inventoryStatus").value("IN_STOCK"))
				.andExpect(jsonPath("$.rating").value(4.5)).andExpect(jsonPath("$.createdAt").exists())
				.andExpect(jsonPath("$.updatedAt").exists());

		verify(productServiceImpl, times(1)).update(any(ProductDto.class));
	}

	@Test
	public void testPatchProduct() throws Exception {
		Long productId = 1L;

		when(productServiceImpl.update(any(ProductDto.class))).thenReturn(productDto);

		String productJson = objectMapper.writeValueAsString(productDto);

		mockMvc.perform(
				patch("/api/v1/products/{id}", productId).contentType(MediaType.APPLICATION_JSON).content(productJson))
				.andExpect(jsonPath("$.name").value("Test Product"))
				.andExpect(jsonPath("$.description").value("product description test"))
				.andExpect(jsonPath("$.image").value("http://ecommerce.alten.ma/productdemo.jpg"))
				.andExpect(jsonPath("$.category").value("Electronics")).andExpect(jsonPath("$.price").value(99.99))
				.andExpect(jsonPath("$.quantity").value(100)).andExpect(jsonPath("$.internalReference").value("REF_98"))
				.andExpect(jsonPath("$.shellId").value(10)).andExpect(jsonPath("$.inventoryStatus").value("IN_STOCK"))
				.andExpect(jsonPath("$.rating").value(4.5)).andExpect(jsonPath("$.createdAt").exists())
				.andExpect(jsonPath("$.updatedAt").exists());

		verify(productServiceImpl, times(1)).update(any(ProductDto.class));
	}

}
