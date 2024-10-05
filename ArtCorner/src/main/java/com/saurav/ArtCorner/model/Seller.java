package com.saurav.ArtCorner.model;

import com.saurav.ArtCorner.domain.UserRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String sellerName;

    @Column(unique = true,nullable = false)
    private String email;

    private String password;

    @Embedded
    private BusinessDetails businessDetails=new BusinessDetails();

    private BankDetails bankDetails=new BankDetails();

    private Address pickupAddress=new Address();

    private String GSTIN;

    private UserRole role=UserRole.ROLE_ADMIN;

    private boolean isEmailVerified=false;
}
