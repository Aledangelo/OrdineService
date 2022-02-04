package com.greenteam.ordineservice.service;

import com.greenteam.ordineservice.entity.Ordine;
import com.greenteam.ordineservice.entity.Prodotto;
import com.greenteam.ordineservice.entity.statoOrdine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.greenteam.ordineservice.repository.ordineRepository;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Optional;

@Service
public class gestioneOrdini {

    @Autowired
    private ordineRepository ordineRepository;

    @Autowired
    private RestTemplate restTemplate;

    public <Any> Any addToOrder(List<Prodotto> prodotti) {
        float tot = 0;

        if (prodotti.size() <= 0) {
            return (Any) "No products";
        }

        try {
            for (Prodotto p: prodotti) {
                tot += p.getPrezzo();
            }

            if (tot <= 0) {
                return (Any) "Operation not permitted";
            }

            Ordine ordine = new Ordine();
            ordine.setNome_ristorante("Rostì");
            ordine.setProdotti(prodotti);
            ordine.setPrezzo_totale(tot);
            return (Any) saveOrdine(ordine);
        } catch (Exception e) {
            return (Any) e.getMessage();
        }
    }

    private Ordine saveOrdine(Ordine o) {
        o.setStato(statoOrdine.IN_ATTESA);
        return ordineRepository.save(o);
    }

    public String removeOrdine(String id) {
        try {
            Optional<Ordine> o = ordineRepository.findById(id);
            if (o.isEmpty()) {
                return "Operation not permitted";
            }
            Ordine ordine = o.get();
            String identifier = ordine.getId();
            ordineRepository.deleteById(identifier);
            return identifier;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String updateStato(String id, statoOrdine s) {
        try {
            Optional<Ordine> o = ordineRepository.findById(id);
            if (o.isEmpty()) {
                return null;
            }
            Ordine ordine = o.get();
            ordine.setStato(s);
            ordineRepository.save(ordine);
            return ordine.getStato().toString();
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public Ordine getOrdine(String id) {
        try {
            return ordineRepository.findById(id).orElseThrow();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Ordine> getAll() {
        try {
            return ordineRepository.findAll();
        } catch (Exception e) {
            return null;
        }
    }
}
