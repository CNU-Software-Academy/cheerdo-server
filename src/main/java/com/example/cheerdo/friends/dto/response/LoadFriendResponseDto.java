package com.example.cheerdo.friends.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class LoadFriendResponseDto {

    private Long relationId;
    private String name;
    private String memberId;

}