package com.saurav.ArtCorner.service;

import com.saurav.ArtCorner.exception.ProductException;
import com.saurav.ArtCorner.model.Product;
import com.saurav.ArtCorner.model.Seller;
import com.saurav.ArtCorner.request.CreateProductRequest;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;
import java.util.List;

public interface ProductService {

    public Product createProduct(CreateProductRequest req, Seller seller);
    public void deleteProduct(Long productId) throws ProductException;
    public Product updateProduct(Long productId,Product product) throws ProductException;
    public Product findProductById(Long productId) throws ProductException;
    public List<Product> searchProduct(String query);
    public List<Product> getAllProduct();
    public Page<Product> getAllProducts(String category,String size,Integer minPrice,Integer maxPrice,String sort,String stock,Integer minDiscount,Integer pageNumber);
    List<Product> getProductBySellerId(Long sellerId);
}
