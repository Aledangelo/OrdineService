package com.greenteam.ordineservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.persistence.Id;

@Document("riders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rider {
    @Id
    private String id;
    private String nome;
    private int eta;
    private String id_ordine;
    private statoRider stato;
}
