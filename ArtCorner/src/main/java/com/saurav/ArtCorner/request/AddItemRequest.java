package com.saurav.ArtCorner.request;

import lombok.Data;

@Data
public class AddItemRequest {
    private int quantity;
    private Long productId;
}
