package com.greenteam.ordineservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
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
    private String nome;
    @NotNull
    private String descrizione;
    @Positive
    private float prezzo;
}
