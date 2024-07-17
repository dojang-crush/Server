package com.team1.dojang_crush.domain.place.domain;
import com.team1.dojang_crush.domain.likePlace.domain.LikePlace;
import com.team1.dojang_crush.domain.post.domain.Post;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_id", updatable = false)
    private Long placeId;

    @Column(name = "theme", length = 20)
    @NotBlank
    private String theme;

    @Column(name = "place_name")
    @NotBlank
    private String placeName;

    @Column(name = "address", length = 500)
    @NotBlank
    private String address;

    @Column(name = "map_id")
    @NotNull
    private Long mapId;

    @OneToMany(mappedBy = "place", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<LikePlace> likedPlaces;

    @OneToMany(mappedBy = "place", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Post> posts;
}