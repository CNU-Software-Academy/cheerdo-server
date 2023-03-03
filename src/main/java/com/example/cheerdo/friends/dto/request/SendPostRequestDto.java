package com.example.cheerdo.friends.dto.request;

import com.example.cheerdo.entity.FriendRelation;
import com.example.cheerdo.entity.PostRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@NoArgsConstructor
public class SendPostRequestDto {
    Long relationId;

    public PostRequest dtoToPostRequestEntity(Optional<FriendRelation> friendRelation, String friendName, String userId) {
        return PostRequest.builder()
                .senderId(friendRelation.get().getFriendId())
                .senderName(friendName)
                .receiverId(userId)
                .sendDateTime(LocalDateTime.now())
                .build();
    }
}