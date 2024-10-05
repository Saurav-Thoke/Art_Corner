package com.saurav.ArtCorner.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Home {

    private List<HomeCategory> paintings;

    private List<HomeCategory> artPieces;

    private List<HomeCategory> craft;

    private List<Deal> deals;
}
