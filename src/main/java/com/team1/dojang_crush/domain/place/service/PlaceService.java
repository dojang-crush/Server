package com.team1.dojang_crush.domain.place.service;

import com.team1.dojang_crush.domain.place.domain.Place;
import com.team1.dojang_crush.domain.place.repository.PlaceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceRepository placeRepository;

    @Transactional(readOnly = true)
    public Place findPlaceById(Long placeId) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(()-> new EntityNotFoundException("해당 id의 장소를 찾을 수 없습니다.id="+placeId));
        return place;
    }
}
