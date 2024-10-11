package com.saurav.ArtCorner.controller;

import com.saurav.ArtCorner.exception.ProductException;
import com.saurav.ArtCorner.model.Product;
import com.saurav.ArtCorner.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId)throws ProductException
    {
        Product product=productService.findProductById(productId);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProduct(@RequestParam(required = false)String query)
    {
        List<Product>products=productService.searchProduct(query);
        return new ResponseEntity<>(products,HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(
            @RequestParam(required = false)String category,
            @RequestParam(required = false)String size,
            @RequestParam(required = false)Integer minPrice,
            @RequestParam(required = false)Integer maxPrice,
            @RequestParam(required = false)String sort,
            @RequestParam(required = false)String stock,
            @RequestParam(required = false)Integer minDiscount,
            @RequestParam(defaultValue = "0")Integer pageNumber)
    {
        return new ResponseEntity<>(productService.getAllProducts(category,size,minPrice,maxPrice,sort,stock,minDiscount,pageNumber),HttpStatus.OK);
    }
}
