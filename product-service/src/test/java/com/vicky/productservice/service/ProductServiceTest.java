package com.vicky.productservice.service;

import com.vicky.productservice.dto.ProductRequest;
import com.vicky.productservice.dto.ProductResponse;
import com.vicky.productservice.model.Product;
import com.vicky.productservice.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCreateProduct() {
        ProductRequest productRequest = new ProductRequest("Test Product", "Test Description", BigDecimal.valueOf(19.99));
        Product savedProduct = Product.builder()
                .id("1")
                .name("Test Product")
                .description("Test Description")
                .price(BigDecimal.valueOf(19.99))
                .build();

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        productService.createProduct(productRequest);

        verify(productRepository, times(1)).save(any(Product.class));
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void testGetAllProduct() {
        List<Product> products = Arrays.asList(
                new Product("1", "Product 1", "Description 1", BigDecimal.valueOf(29.99)),
                new Product("2", "Product 2", "Description 2", BigDecimal.valueOf(39.99))
        );

        when(productRepository.findAll()).thenReturn(products);

        List<ProductResponse> productResponses = productService.getAllProduct();

        assertThat(productResponses).hasSize(2);
        assertThat(productResponses.get(0).getId()).isEqualTo("1");
        assertThat(productResponses.get(0).getName()).isEqualTo("Product 1");
        assertThat(productResponses.get(0).getDescription()).isEqualTo("Description 1");
        assertThat(productResponses.get(0).getPrice()).isEqualTo(BigDecimal.valueOf(29.99));

        assertThat(productResponses.get(1).getId()).isEqualTo("2");
        assertThat(productResponses.get(1).getName()).isEqualTo("Product 2");
        assertThat(productResponses.get(1).getDescription()).isEqualTo("Description 2");
        assertThat(productResponses.get(1).getPrice()).isEqualTo(BigDecimal.valueOf(39.99));

        verify(productRepository, times(1)).findAll();
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void testCreateProductWithRepositoryException() {
        // Prepare the input for the test: create a ProductRequest with sample data
        ProductRequest productRequest = new ProductRequest("Test Product", "Test Description", BigDecimal.valueOf(19.99));

        // Set up the behavior of the mocked productRepository
        // When the save method of the repository is called with any Product object,
        // Then throw an exception (simulate an error during save).
        when(productRepository.save(any(Product.class))).thenThrow(new RuntimeException("Repository error"));

        // Invoke the method being tested: call the createProduct method of the productService with the prepared input
        // and expect an exception to be thrown.
        assertThrows(RuntimeException.class, () -> productService.createProduct(productRequest));

        // Verify the interactions with the mocked productRepository:
        // Ensure that the save method of the repository was called exactly once with any Product object
        verify(productRepository, times(1)).save(any(Product.class));

        // Verify that there are no more interactions with the mocked productRepository
        verifyNoMoreInteractions(productRepository);
    }
}
