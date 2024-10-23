package com.saurav.ArtCorner.service;

import com.saurav.ArtCorner.model.Home;
import com.saurav.ArtCorner.model.HomeCategory;

import java.util.List;

public interface HomeService {
    public Home createHomePageData(List<HomeCategory> allCategories);
}
