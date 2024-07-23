package com.team1.dojang_crush.domain.place.service;

import com.team1.dojang_crush.domain.place.domain.Place;
import com.team1.dojang_crush.domain.place.repository.PlaceRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@Transactional
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;


    public List<Place> findAllPlace() {
        List<Place> places = placeRepository.findAll();
        return places;
    }


    public List<Place> findPlaceByTheme(String theme) {
        return placeRepository.findByTheme(theme);

    }


}
