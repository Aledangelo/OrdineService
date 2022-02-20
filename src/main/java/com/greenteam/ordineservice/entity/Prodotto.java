package com.greenteam.ordineservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.persistence.Id;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@Document("prodotti")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Prodotto {
    @Id
    private String id;
    @NotNull
    @Pattern(regexp = "[a-zA-Z0-9]+")
    private String nome;
    @NotNull
    @Pattern(regexp = "[a-zA-Z0-9]+")
    private String descrizione;
    @Positive
    private float prezzo;
}
