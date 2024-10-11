package com.saurav.ArtCorner.service.implementation;

import com.saurav.ArtCorner.exception.ProductException;
import com.saurav.ArtCorner.model.Category;
import com.saurav.ArtCorner.model.Product;
import com.saurav.ArtCorner.model.Seller;
import com.saurav.ArtCorner.repository.CategoryRepository;
import com.saurav.ArtCorner.repository.ProductRepository;
import com.saurav.ArtCorner.request.CreateProductRequest;
import com.saurav.ArtCorner.service.ProductService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImplementation implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    @Override
    public Product createProduct(CreateProductRequest req, Seller seller) {
        Category category1=categoryRepository.findByCategoryId(req.getCategory());
        if(category1==null)
        {
            Category category=new Category();
            category.setCategoryId(req.getCategory());
            category.setLevels(1);
            category1=categoryRepository.save(category);
        }

        Category category2=categoryRepository.findByCategoryId(req.getCategory2());
        if(category2==null)
        {
            Category category=new Category();
            category.setCategoryId(req.getCategory2());
            category.setLevels(2);
            category.setParentCategory(category1);
            category2=categoryRepository.save(category);
        }

        Category category3=categoryRepository.findByCategoryId(req.getCategory3());
        if(category3==null)
        {
            Category category=new Category();
            category.setCategoryId(req.getCategory3());
            category.setLevels(3);
            category.setParentCategory(category2);
            category3=categoryRepository.save(category);
        }
        int discountPercentage=calculateDiscountPercentage(req.getMrpPrice(),req.getSellingPrice());

        Product product=new Product();
        product.setCategory(category3);
        product.setDescription(req.getDescription());
        product.setCreatedAt(LocalDateTime.now());
        product.setTitle(req.getTitle());
        product.setSellingPrice(req.getSellingPrice());
        product.setImages(req.getImages());
        product.setMrpPrice(req.getMrpPrice());
        product.setDiscountPercent(discountPercentage);
        return productRepository.save(product);
    }

    public int calculateDiscountPercentage(int mrpPrice, int sellingPrice) {
        if(mrpPrice<=0)
        {
            return 0;
        }
        double discount=mrpPrice-sellingPrice;
        double discountPercentage=(discount/mrpPrice)*100;
        return (int)discountPercentage;
    }

    @Override
    public void deleteProduct(Long productId) throws ProductException {
        Product product=findProductById(productId);
        productRepository.delete(product);
    }

    @Override
    public Product updateProduct(Long productId, Product product) throws ProductException {
        findProductById(productId);
        product.setId(productId);

        return productRepository.save(product);
    }

    @Override
    public Product findProductById(Long productId) throws ProductException {
        return productRepository.findById(productId).orElseThrow(()->new ProductException("Prodct not found with id"+productId));
    }

    @Override
    public List<Product> searchProduct(String query) {
        return productRepository.searchProduct(query);
    }

    @Override
    public List<Product> getAllProduct() {
        return null;
    }

    @Override
    public Page<Product> getAllProducts(String category, String size, Integer minPrice, Integer maxPrice, String sort, String stock,Integer minDiscount,Integer pageNumber)
    {
        Specification<Product> specification=(root, query, criteriaBuilder) -> {
            List<Predicate>predicates=new ArrayList<>();
            if(category!=null)
            {
                Join<Product,Category> categoryJoin= root.join("category");
                predicates.add(criteriaBuilder.equal(categoryJoin.get("categoryId"),category));
            }
            if(size!=null)
            {
                predicates.add(criteriaBuilder.equal(root.get("size"),size));
            }
            if(minPrice!=null)
            {
                predicates.add(criteriaBuilder.equal(root.get("sellingPrice"),minPrice));
            }
            if(maxPrice!=null)
            {
                predicates.add(criteriaBuilder.equal(root.get("sellingPrice"),maxPrice));
            }
            if(minDiscount!=null)
            {
                predicates.add(criteriaBuilder.equal(root.get("discountPercentage"),minDiscount));
            }
            if(stock!=null)
            {
                predicates.add(criteriaBuilder.equal(root.get("stock"),stock));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

       Pageable pageable;
        if(sort!=null && !sort.isEmpty())
        {
            pageable = switch (sort) {
                case "price_low" ->
                        PageRequest.of(pageNumber != null ? pageNumber : 0, 10, Sort.by("sellingPrice").ascending());
                case "price_high" ->
                        PageRequest.of(pageNumber != null ? pageNumber : 0, 10, Sort.by("sellingPrice").descending());
                default -> PageRequest.of(pageNumber != null ? pageNumber : 0, 10, Sort.unsorted());
            };
        }
        else
        {
            pageable=PageRequest.of(pageNumber!=null?pageNumber:0,10,Sort.unsorted());
        }
        return productRepository.findAll(specification,pageable);
    }

    @Override
    public List<Product> getProductBySellerId(Long sellerId) {
        return productRepository.findBySellerId(sellerId);
    }
}
