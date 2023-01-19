package com.example.cheerdo.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
@Getter
@NoArgsConstructor
public class Member {
    @Id
    @Column(name = "member_id")
    private String id;

    @Column(name = "member_pw")
    private String password;

    private String name;

    @Lob
    private String bio;

    @Column(name = "coin_count")
    private int coinCount;

    @Column(name = "habit_progress")
    private double habitProgress;

    @Builder
    public Member(String id, String password, String name, String bio, int coinCount, double habitProgress) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.bio = bio;
        this.coinCount = coinCount;
        this.habitProgress = habitProgress;
    }
}