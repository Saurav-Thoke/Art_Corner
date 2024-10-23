package com.saurav.ArtCorner.service.implementation;

import com.saurav.ArtCorner.model.Deal;
import com.saurav.ArtCorner.model.HomeCategory;
import com.saurav.ArtCorner.repository.DealRepository;
import com.saurav.ArtCorner.repository.HomeCategoryRepository;
import com.saurav.ArtCorner.service.DealService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DealServiceImplementation implements DealService {
    private final DealRepository dealRepository;
    private final HomeCategoryRepository homeCategoryRepository;

    @Override
    public List<Deal> getDeals() {
        return dealRepository.findAll();
    }

    @Override
    public Deal createDeal(Deal deal) {
        HomeCategory category=homeCategoryRepository.findById(deal.getCategory().getId()).orElseThrow(null);
        Deal newDeal=dealRepository.save(deal);
        newDeal.setCategory(category);
        newDeal.setDiscount(deal.getDiscount());
        return dealRepository.save(newDeal);
    }

    @Override
    public Deal updateDeal(Deal deal,Long id) throws Exception {
        Deal existingDeal=dealRepository.findById(id).orElse(null);
        HomeCategory category=homeCategoryRepository.findById(deal.getCategory().getId()).orElse(null);
        if(existingDeal!=null)
        {
            if(deal.getDiscount()!=null)
            {
                existingDeal.setDiscount(deal.getDiscount());
            }
            if(category!=null)
            {
                existingDeal.setCategory(deal.getCategory());
            }
            return dealRepository.save(existingDeal);
        }
        throw new Exception("Deal not found");
    }

    @Override
    public void deleteDeal(Long id) throws Exception {
        Deal deal=dealRepository.findById(id).orElseThrow(()->new Exception("Deal not found"));
        dealRepository.delete(deal);

    }
}
