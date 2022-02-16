package com.greenteam.ordineservice.service;

import com.greenteam.ordineservice.entity.Ordine;
import com.greenteam.ordineservice.entity.Prodotto;
import com.greenteam.ordineservice.entity.statoOrdine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.greenteam.ordineservice.repository.ordineRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class gestioneOrdini {

    @Autowired
    private ordineRepository ordineRepository;

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

    public void setRiderOrdine(String rider, String ordine) {
        try {
            Optional<Ordine> o = ordineRepository.findById(ordine);
            if (o.isEmpty()) {
                return;
            }
            Ordine ord = o.get();
            ord.setNome_rider(rider);
            ordineRepository.save(ord);
            return;
        } catch (Exception e) {
            return;
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

    public List<Ordine> getWaiting() {
        List<Ordine> l = ordineRepository.findAll();
        List<Ordine> ordini = new ArrayList<Ordine>();
        for (Ordine o: l) {
            if (o.getStato() == statoOrdine.IN_ATTESA) {
                ordini.add(o);
            }
        }
        return ordini;
    }
}
