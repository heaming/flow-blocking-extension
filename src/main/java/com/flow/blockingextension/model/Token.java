package com.flow.blockingextension.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "token")
public class Token extends BaseTimeEntity {
    @Id
    private Long id;

    @Column(nullable = false)
    private String token;
    @Column
    private LocalDateTime dateDeleted;

}
