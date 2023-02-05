package com.example.cheerdo.friendList.controller;

import com.example.cheerdo.friendList.dto.request.SendRequestDto;
import com.example.cheerdo.friendList.dto.response.LoadFriendResponseDto;
import com.example.cheerdo.friendList.service.FriendRelationService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/friend")
@RequiredArgsConstructor
public class FriendsController {
    private final Logger logger = LoggerFactory.getLogger(FriendsController.class);
    private final FriendRelationService friendRelationService;
    @GetMapping(value = "/List{userId}")
    @ApiOperation(value = "userId의 초기 Friend 화면에 필요한 data를 가져오는 api"
            , notes = "반환값으로 relationId memberId name list가 반환된다")
    @ApiResponse(code = 200, message = "status ok")
    public ResponseEntity<?> getMyFriendList(@PathVariable("userId") String userId) {
        logger.info("request : UserID -> {}", userId);
        try {
            List<LoadFriendResponseDto> LoadFriendResponseDtos = friendRelationService.getMyFriendList(userId);
            return new ResponseEntity<>(LoadFriendResponseDtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "받은 친구요청을 가져오는 API"
            , notes = "반환값으로 relationId memberId name list가 반환된다")
    @PostMapping(value = "/getrequest/{userId}")
    @ApiResponse(code = 200, message = "status ok")
    public ResponseEntity<?> getMyRequest(@PathVariable("userId") String userId) {
        logger.info("request is -> {}", userId);
        try {
            //friendRelationService.getMyRequest(userId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "친구 요청을 보내는 api"
            , notes = "반환값으로 Http status가 반환된다.")
    @PostMapping(value = "/putRquest")
    @ApiResponse(code = 200, message = "status ok")
    public ResponseEntity<?> putRequest(@RequestBody PutRequestDto putRequestDto) {
        logger.info("request is -> {}", putRequestDto.toString());
        try {
            friendRelationService.putRequest(putRequestDto);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
