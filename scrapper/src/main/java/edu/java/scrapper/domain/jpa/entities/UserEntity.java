package edu.java.scrapper.domain.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@Table(name = "USERS")
@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public @Entity class UserEntity {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

}

