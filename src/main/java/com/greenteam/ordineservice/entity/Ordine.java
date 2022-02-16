package com.greenteam.ordineservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.persistence.Id;
import javax.validation.constraints.Positive;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document("ordini")
public class Ordine {
    @Id
    private String id;
    private String nome_ristorante;
    private String nome_rider;
    @NotNull
    private List<Prodotto> prodotti;
    @Positive
    private float prezzo_totale;
    private statoOrdine stato;
}
