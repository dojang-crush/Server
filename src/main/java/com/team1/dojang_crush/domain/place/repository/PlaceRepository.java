package com.team1.dojang_crush.domain.place.repository;

import com.team1.dojang_crush.domain.place.domain.Place;
import com.team1.dojang_crush.domain.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    List<Place> findByTheme(String theme);

    List<Place> findByPlaceNameContaining(String searchKeyword);

}
