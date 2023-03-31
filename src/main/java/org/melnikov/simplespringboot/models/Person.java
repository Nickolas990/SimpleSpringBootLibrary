package org.melnikov.simplespringboot.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "full_name", nullable = false, length = Integer.MAX_VALUE)
    @NotEmpty
    private String fullName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "loyalty")
    @Enumerated(EnumType.ORDINAL)
    private Status loyalty;

    @OneToMany(mappedBy = "client")
    private List<Book> books;

}