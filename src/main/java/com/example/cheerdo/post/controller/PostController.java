package com.example.cheerdo.post.controller;

import com.example.cheerdo.post.dto.request.LetterRequestDto;
import com.example.cheerdo.post.dto.request.PostRequestDto;
import com.example.cheerdo.post.dto.response.PostResponseDto;
import com.example.cheerdo.post.dto.response.PostStatusResponse;
import com.example.cheerdo.post.dto.response.LetterResponseDto;
import com.example.cheerdo.post.service.PostService;
import com.github.javafaker.Faker;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("api/post")
@RequiredArgsConstructor
public class PostController {
    private final Logger logger = LoggerFactory.getLogger(PostController.class);
    private final PostService postService;

    @GetMapping("/posts")
    @ApiOperation(value = "PostBox를 눌렀을때 나오는 읽지 않은 편지들을 반환"
            , notes = "사용자의 ID를 통해 조회한다. ")
    @ApiResponses( {
            @ApiResponse( code = 200, message = "status ok")
    })
    public ResponseEntity<?> getMyNewPost(@ModelAttribute PostRequestDto postRequestDto) {
        logger.info("request is -> {}", postRequestDto);
        try {
            List<PostResponseDto> postResponseDtos = postService.getMyPosts(postRequestDto);
            return new ResponseEntity<>(postResponseDtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/letter")
    public ResponseEntity<?> readLetter(@RequestParam Long letterId) {
        return new ResponseEntity<>(postService.readPost(letterId), HttpStatus.OK);
    }

    @PostMapping("/letter")
    public ResponseEntity<?> writeLetter(@RequestBody LetterRequestDto letterRequestDto) {
        postService.writeLetter(letterRequestDto);
        return new ResponseEntity<>("편지를 성공적으로 보냈습니다", HttpStatus.OK);
    }

    @GetMapping("/status")
    public ResponseEntity<?> getStatus(@RequestParam Long userId) {
        logger.info("request userId is -> {}", userId);
        return new ResponseEntity<>(
                new PostStatusResponse(10, 100), HttpStatus.OK);
    }
}