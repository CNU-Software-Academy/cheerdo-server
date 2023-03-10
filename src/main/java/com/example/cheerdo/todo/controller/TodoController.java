package com.example.cheerdo.todo.controller;

import com.example.cheerdo.entity.Todo;
import com.example.cheerdo.todo.dto.request.GetTodoRequestDto;
import com.example.cheerdo.todo.dto.request.ModifyTodoRequestDto;
import com.example.cheerdo.todo.dto.request.WriteTodoRequestDto;
import com.example.cheerdo.todo.dto.response.TodoResponseDto;
import com.example.cheerdo.todo.service.TodoService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/// TODO: 2023-02-02
/*
    에러 처리를 위해 너무 많은 try catch와 와일드카드가 사용되었다.
    Controller Advice와 Exception Handler로 refactoring하기
*/
@RestController
@AllArgsConstructor
@RequestMapping("/api/member/todo")
public class TodoController {

    private final TodoService todoService;
    private final Logger logger = LoggerFactory.getLogger(TodoController.class);

    @ApiOperation(value = "Todo를 생성하는 api"
            , notes = "반환값으로 Todo의 id가 반환된다. ")
    @PostMapping("")
    public ResponseEntity<String> writeTodo(@RequestBody WriteTodoRequestDto todoDto) {
        return new ResponseEntity<>(todoService.writeTodo(todoDto), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Todo를 Get하는 api"
            , notes = "todo list들이 반환된다. ")
    @GetMapping("")
    public ResponseEntity<List<TodoResponseDto>> getMyTodo(@ModelAttribute GetTodoRequestDto getTodoRequestDto) {
        logger.info("input userID is -> {}", getTodoRequestDto.getMemberId());
        return new ResponseEntity<>(todoService.getMyTodos(getTodoRequestDto), HttpStatus.OK);

    }

    @ApiOperation(value = "Todo를 put(modify)하는 api"
            , notes = "반환값으로 Todo의 id가 반환된다.")
    @PutMapping("")
    public ResponseEntity<?> modifyTodo(@RequestBody ModifyTodoRequestDto modifyTodoRequestDto) {
        try {
            todoService.modifyTodo(modifyTodoRequestDto);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation(value = "Todo를 delete하는 api"
            , notes = "반환값이 존재하지 않는다 error 시 error message 반환")
    @DeleteMapping("")
    public ResponseEntity<?> deleteTodo(@RequestParam String todoId) {
        try {
            todoService.deleteTodo(todoId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation(value = "Todo 성공체크시 요청되는 api"
            , notes = "반환값이 존재하지 않는다 error 시 error message 반환")
    @PutMapping("/success")
    public ResponseEntity<?> updateSuccess(@RequestParam String todoId) {
        try {
            todoService.success(todoId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
