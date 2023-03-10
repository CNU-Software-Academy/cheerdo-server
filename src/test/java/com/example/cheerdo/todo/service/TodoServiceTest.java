package com.example.cheerdo.todo.service;

import com.example.cheerdo.entity.Calender;
import com.example.cheerdo.entity.Member;
import com.example.cheerdo.entity.Todo;
import com.example.cheerdo.repository.CalenderRepository;
import com.example.cheerdo.repository.MemberRepository;
import com.example.cheerdo.repository.TodoRepository;
import com.example.cheerdo.todo.dto.request.GetTodoRequestDto;
import com.example.cheerdo.todo.dto.request.ModifyTodoRequestDto;
import com.example.cheerdo.todo.dto.request.WriteTodoRequestDto;
import com.example.cheerdo.todo.dto.response.TodoResponseDto;
import com.example.cheerdo.entity.enums.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@Rollback(true)
@RunWith(SpringRunner.class)
class TodoServiceTest {
    private final Logger logger = LoggerFactory.getLogger(TodoServiceTest.class);
    @Autowired
    private TodoService todoService;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CalenderRepository calenderRepository;

    private Todo todo;

    private Member member;

    private String testMemberId = "kim11111";

    private static final LocalDate testDate = LocalDate.of(1999, 5, 5);

    @BeforeEach
    void init() {
        // ????????? Member
        member = Member.builder()
                .id(testMemberId)
                .name("?????????")
                .build();
        memberRepository.save(member);
        // ????????? Todo
        todo = Todo.builder()
                .todoId(UUID.randomUUID().toString())
                .isSuccess(false)
                .type(Type.TODO)
                .content("????????????")
                .build();
        todoRepository.save(todo);
    }

    @Test
    @DisplayName("Todo??? ?????? ????????? ?????? ????????? ?????? calender??? ????????? ???????????? calender?????? todos??? todo??? ????????????.  ")
    void writeTodo() {
        // given
        WriteTodoRequestDto writeTodoRequestDto =
                new WriteTodoRequestDto(UUID.randomUUID().toString()
                        , testMemberId
                        , Type.TODO.name()
                        , "15:50"
                        , "?????????"
                        , testDate);

        // when
        String todoId = todoService.writeTodo(writeTodoRequestDto);
        Todo todo = todoRepository.findById(todoId).get();
        Calender calender = todo.getCalender();
        // then
        assertEquals(todo.getContent(), writeTodoRequestDto.getTodo());
        assertEquals(todo.getType().toString(), writeTodoRequestDto.getType());

        // calender??? ??? ?????? ????????????
        assertEquals(calender, calenderRepository.findById(calender.getCalenderId()).get());
        // calender??? Todos??? ??????????????????.
        assertThat(calender.getTodos(), contains(todo));
        assertThat(calenderRepository.findById(calender.getCalenderId()).get().getTodos(), contains(todo));


    }


    @Test
    @DisplayName("????????? ????????? Todo??? ??????????????? ????????????. ")
    void getMyTodos() {
        // given
        WriteTodoRequestDto writeTodoRequestDto =
                new WriteTodoRequestDto(UUID.randomUUID().toString()
                        , testMemberId
                        , Type.TODO.name()
                        , "15:50"
                        , "?????????"
                        , testDate);
        String todoId = todoService.writeTodo(writeTodoRequestDto);

        GetTodoRequestDto getTodoRequestDto =
                new GetTodoRequestDto(testMemberId, Type.TODO.name(), testDate);

        // when
        List<TodoResponseDto> todoResponseDtos = todoService.getMyTodos(getTodoRequestDto);

        // then
        assertThat(todoResponseDtos.size(), is(1));

    }

    @Test
    @DisplayName("?????? ????????? ????????? ???????????? ???????????? ??? Array??? ????????????.  ")
    void getEmptyTodos() {
        // given
        GetTodoRequestDto emptyGetTodoRequestDto =
                new GetTodoRequestDto(testMemberId, Type.TODO.name(), LocalDate.of(1111, 1, 1));

        // when
        List<TodoResponseDto> emptyResponseDtos = todoService.getMyTodos(emptyGetTodoRequestDto);

        // then
        assertThat(emptyResponseDtos, empty());
    }

    @Test
    @DisplayName("????????? ??? ?????????????????? ????????????. ")
    void modifyTodo() {
        // given
        WriteTodoRequestDto writeTodoRequestDto =
                new WriteTodoRequestDto(UUID.randomUUID().toString()
                        , testMemberId
                        , Type.TODO.name()
                        , "15:50"
                        , "?????????"
                        , testDate);
        String todoId = todoService.writeTodo(writeTodoRequestDto);

        // when
        todoService.modifyTodo(new ModifyTodoRequestDto(todoId, "????????? ??????"));

        // then
        Todo todo = todoRepository.findById(todoId).get();
        assertThat(writeTodoRequestDto.getTodo(), not(todo.getContent()));
        assertThat(todoId, is(todo.getTodoId()));
    }

    @Test
    @DisplayName("Todo??? ????????? ??? ??????. ")
    void deleteTodo() {
        // given
        WriteTodoRequestDto writeTodoRequestDto =
                new WriteTodoRequestDto(UUID.randomUUID().toString()
                        , testMemberId
                        , Type.TODO.name()
                        , "15:50"
                        , "?????????"
                        , testDate);
        String todoId = todoService.writeTodo(writeTodoRequestDto);

        // when
        todoService.deleteTodo(todoId);

        // then
        Optional<Todo> todo = todoRepository.findById(todoId);
        assertThat(todo.isEmpty(), is(true));
        assertThat(todo.isPresent(), is(false));
    }

    @Test
    @DisplayName("success rate??? update??? ??? ??????. ")
    void success() {
        // given
        WriteTodoRequestDto writeTodoRequestDto =
                new WriteTodoRequestDto(UUID.randomUUID().toString()
                        , testMemberId
                        , Type.TODO.name()
                        , "15:50"
                        , "?????????"
                        , testDate);
        String todoId = todoService.writeTodo(writeTodoRequestDto);

        // when
        todoService.success(todoId);

        // then
        Calender calender = todoRepository.findById(todoId).get().getCalender();
        logger.info("rate is -> {}", calender.getSuccessRate());
        assertThat(calender.getTodos().size(), is(1));
        assertThat(calender.getSuccessRate(), is(100.0));

    }
}