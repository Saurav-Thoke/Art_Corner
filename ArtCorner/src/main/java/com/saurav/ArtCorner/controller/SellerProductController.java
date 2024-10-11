package com.saurav.ArtCorner.controller;

import com.saurav.ArtCorner.model.Product;
import com.saurav.ArtCorner.model.Seller;
import com.saurav.ArtCorner.request.CreateProductRequest;
import com.saurav.ArtCorner.service.ProductService;
import com.saurav.ArtCorner.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seller/products")
public class SellerProductController {

    private final SellerService sellerService;
    private final ProductService productService;

    @PostMapping()
    public ResponseEntity<Product> createProduct(@RequestBody CreateProductRequest request,
                                                 @RequestHeader("Authorization")String jwt)
                                                 throws Exception
    {
        Seller seller=sellerService.getSellerProfile(jwt);

        Product product=productService.createProduct(request,seller);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Product> deleteProduct(@PathVariable Long productId)
    {
        try{
            productService.deleteProduct(productId);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long productId,@RequestBody Product product)
    {
        try{
            Product updatedProduct=productService.updateProduct(productId,product);
            return new ResponseEntity<>(updatedProduct,HttpStatus.OK);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
