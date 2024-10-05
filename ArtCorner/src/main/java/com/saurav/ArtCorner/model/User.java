package com.saurav.ArtCorner.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.saurav.ArtCorner.domain.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)   //whenever this data is fetched, password is not fetched
    private String password;

    private String email;


    private String fullName;

    private String mobile;

    private UserRole role=UserRole.ROLE_CUSTOMER;

    @OneToMany
    private Set<Address> addresses=new HashSet<>();

}
