package edu.java.domain.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@Table(name = "LINKS")
@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public @Entity class LinkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "link_type", nullable = false)
    private LinkType linkType;

    @Column(name = "url", unique = true, nullable = false)
    private String url;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_update")
    private Timestamp lastUpdate;

}
