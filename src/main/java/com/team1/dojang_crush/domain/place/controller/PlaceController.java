package com.team1.dojang_crush.domain.place.controller;

import com.team1.dojang_crush.domain.place.domain.Place;
import com.team1.dojang_crush.domain.place.dto.AllPlacesResponseDto;
import com.team1.dojang_crush.domain.place.dto.PlaceResponseDto;
import com.team1.dojang_crush.domain.place.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;




@RestController
@RequiredArgsConstructor
@RequestMapping("/place")
public class PlaceController {

    private final PlaceService placeService;

    // 장소 전체 조회
    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public AllPlacesResponseDto getAllPlaces() {
        List<PlaceResponseDto> list = new ArrayList<>();
        List<Place> places = placeService.findAllPlace();
        for (Place place : places) {
            PlaceResponseDto dto = PlaceResponseDto.from(place);
            list.add(dto);
        }

        return new AllPlacesResponseDto(list);

    }


    // 장소 테마별 조회
    @GetMapping("{theme}")
    @ResponseStatus(value = HttpStatus.OK)
    public AllPlacesResponseDto getThemePlaces(@PathVariable(name = "theme") String theme) {
        List<PlaceResponseDto> list = new ArrayList<>();
        List<Place> places = placeService.findPlaceByTheme(theme);
        for (Place place : places) {
            PlaceResponseDto dto = PlaceResponseDto.from(place);
            list.add(dto);
        }

        return new AllPlacesResponseDto(list);

    }



}
