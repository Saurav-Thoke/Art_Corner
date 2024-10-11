package com.saurav.ArtCorner.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long sellerId;
    private String title;
    private String description;
    private int mrpPrice;
    private int sellingPrice;
    private int discountPercent;
    private int quantity;
    private String sizes;

    @ElementCollection
    private List<String> images=new ArrayList<>();

    private int numRating;

    @ManyToOne
    private Category category;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Review> reviews=new ArrayList<>();


}
