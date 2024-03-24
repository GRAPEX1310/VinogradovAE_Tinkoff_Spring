package edu.java.domain.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Table(name = "USERS")
@Builder
@Getter
@Setter
@AllArgsConstructor
public @Entity class UserEntity {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

}

