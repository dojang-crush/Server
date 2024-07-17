package com.team1.dojang_crush.domain.comment.domain.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentCreatedRequestDTO {
    @NotNull
    @Max(value = 500)
    String content;

    @Nullable
    Long parentId;

}
