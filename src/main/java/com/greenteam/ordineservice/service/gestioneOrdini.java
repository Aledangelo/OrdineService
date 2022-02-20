package com.greenteam.ordineservice.service;

import com.greenteam.ordineservice.entity.Ordine;
import com.greenteam.ordineservice.entity.Prodotto;
import com.greenteam.ordineservice.entity.statoOrdine;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
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

    public void addToOrder(List<Prodotto> prodotti, String id_utente, String address) throws Exception {
        float tot = 0;

        if (prodotti.size() <= 0) {
            throw new Exception();
        }

        for (Prodotto p: prodotti) {
            tot += p.getPrezzo();
        }

        if (tot <= 0) {
            throw new Exception();
        }

        Ordine ordine = new Ordine();
        ordine.setNome_ristorante("Rostì");
        ordine.setProdotti(prodotti);
        ordine.setPrezzo_totale(tot);
        ordine.setId_user(id_utente);
        ordine.setIndirizzo(address);
        saveOrdine(ordine);
    }

    private Ordine saveOrdine(Ordine o) throws Exception {
        o.setStato(statoOrdine.IN_ATTESA);
        return ordineRepository.save(o);
    }

    public String removeOrdine(String id) throws Exception{
        if (ObjectId.isValid(id)) {
            Optional<Ordine> o = ordineRepository.findById(id);
            if (o.isEmpty()) {
                throw new Exception();
            }
            Ordine ordine = o.get();
            String identifier = ordine.getId();
            ordineRepository.deleteById(identifier);
            return identifier;
        } else throw new Exception();
    }

    public void setRiderOrdine(String rider, String ordine) throws Exception {
        if (ObjectId.isValid(ordine)) {
            Optional<Ordine> o = ordineRepository.findById(ordine);
            if (o.isEmpty()) {
                throw new Exception();
            }
            Ordine ord = o.get();
            ord.setNome_rider(rider);
            ordineRepository.save(ord);
        } else throw new Exception();
    }

    public String updateStato(String id, statoOrdine s) throws Exception{
        if (ObjectId.isValid(id)) {
            Optional<Ordine> o = ordineRepository.findById(id);
            if (o.isEmpty()) {
                throw new Exception();
            }
            Ordine ordine = o.get();
            ordine.setStato(s);
            ordineRepository.save(ordine);
            return ordine.getStato().toString();
        } else throw new Exception();
    }

    public Ordine getOrdine(String id) throws Exception{
        if (ObjectId.isValid(id)) return ordineRepository.findById(id).orElseThrow();
        else throw new Exception();
    }

    public List<Ordine> getAll() throws Exception {
        return ordineRepository.findAll();
    }

    public List<Ordine> getWaiting() throws Exception{
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
