package com.saurav.ArtCorner.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.cglib.beans.FixedKeySet;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private User user;
    @OneToMany(mappedBy = "cart",cascade = CascadeType.ALL,orphanRemoval = true)   //cascade -any changes made to cartItem it will be reflected  orpanRemoval-any item removed will be reflected
    private Set<CartItem> cartItems=new HashSet<>();

    private double totalSellingPrice;
    private int totalItem;
    private int totalMrpPrice;
}
