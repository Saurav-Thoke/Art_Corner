package com.saurav.ArtCorner.repository;

import com.saurav.ArtCorner.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address,Long> {
}
