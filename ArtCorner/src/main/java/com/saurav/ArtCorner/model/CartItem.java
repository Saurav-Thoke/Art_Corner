package com.saurav.ArtCorner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JsonIgnore    //It is used to prevent certain fields from being included in the JSON output during serialization or being processed during deserialization
    private Cart cart;

    @ManyToOne
    private Product product;

    private Integer mrpPrice;

    private Integer sellingPrice;

    private Long userId;

}
