package com.example.cheerdo.post.repository;

import com.example.cheerdo.entity.FriendRelation;
import com.example.cheerdo.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<List<Post>> findAllByReceiverIdAndIsOpenOrderBySendDateTime(@Param("memberId")String memberId, @Param("isOpen") boolean isOpen);

}