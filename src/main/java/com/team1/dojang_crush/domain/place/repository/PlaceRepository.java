package com.team1.dojang_crush.domain.place.repository;

import com.team1.dojang_crush.domain.place.domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {
}
