package com.team1.dojang_crush.domain.place.service;

import com.team1.dojang_crush.domain.place.domain.Place;
import com.team1.dojang_crush.domain.place.dto.PlaceResponseDto;
import com.team1.dojang_crush.domain.place.repository.PlaceRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

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


    @Transactional(readOnly = true)
    public Place findPlaceById(Long placeId) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(()-> new EntityNotFoundException("해당 id의 장소를 찾을 수 없습니다.id="+placeId));
        return place;
    }


    @Transactional
    public List<PlaceResponseDto> searchPlace(String searchKeyword) {
        List<Place> placeList= placeRepository.findByPlaceNameContaining(searchKeyword);

        if(placeList == null){
            return null;
        }
        else{
            List<PlaceResponseDto> list = new ArrayList<>();
            for(Place p : placeList){
                PlaceResponseDto dto = PlaceResponseDto.from(p);
                list.add(dto);
            }
            return list;
        }
    }
}
