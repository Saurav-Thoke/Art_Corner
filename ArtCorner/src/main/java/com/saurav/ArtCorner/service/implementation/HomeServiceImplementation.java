package com.saurav.ArtCorner.service.implementation;

import com.saurav.ArtCorner.model.Deal;
import com.saurav.ArtCorner.model.Home;
import com.saurav.ArtCorner.model.HomeCategory;
import com.saurav.ArtCorner.model.HomeCategorySection;
import com.saurav.ArtCorner.repository.DealRepository;
import com.saurav.ArtCorner.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomeServiceImplementation implements HomeService {

    private final DealRepository dealRepository;
    @Override
    public Home createHomePageData(List<HomeCategory> allCategories) {
        List<HomeCategory> gridCategories=allCategories.stream().filter(category->category.getSection()== HomeCategorySection.GRID)
                .collect(Collectors.toList());

        List<HomeCategory>paintingCategories=allCategories.stream().filter(category->category.getSection()==HomeCategorySection.PAINTINGS)
                .collect(Collectors.toList());

        List<HomeCategory>artPiecesCategories=allCategories.stream().filter(category->category.getSection()==HomeCategorySection.ART_PIECES)
                .collect(Collectors.toList());

        List<HomeCategory>craftCategories=allCategories.stream().filter(category->category.getSection()==HomeCategorySection.CRAFT)
                .collect(Collectors.toList());

        List<HomeCategory>dealCategories=allCategories.stream().filter(category->category.getSection()==HomeCategorySection.DEALS)
                .collect(Collectors.toList());

        List<Deal> createdDeals=new ArrayList<>();
        if(dealRepository.findAll().isEmpty()) {
            List<Deal> deals = allCategories.stream()
                    .filter(category -> category.getSection() == HomeCategorySection.DEALS)
                    .map(category -> new Deal(null, 10, category))
                    .collect(Collectors.toList());
            createdDeals=dealRepository.saveAll(deals);

        }
        else
        {
            createdDeals =dealRepository.findAll();
        }


            Home home=new Home();
            home.setGrid(gridCategories);
            home.setCraft(craftCategories);
            home.setPaintings(paintingCategories);
            home.setArtPieces(artPiecesCategories);
            home.setDeals(createdDeals);


        return home;
    }
}
