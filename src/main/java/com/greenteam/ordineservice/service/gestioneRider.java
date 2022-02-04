package com.greenteam.ordineservice.service;

import com.greenteam.ordineservice.entity.Ordine;
import com.greenteam.ordineservice.entity.Rider;
import com.greenteam.ordineservice.entity.statoOrdine;
import com.greenteam.ordineservice.entity.statoRider;
import com.greenteam.ordineservice.repository.riderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class gestioneRider {
    @Autowired
    private riderRepository riderRepository;

    @Autowired
    private gestioneOrdini gestioneOrdini;

    public String accettaOrdine(String id_rider, String id_ordine) {
        try {
            Optional<Rider> r = riderRepository.findById(id_rider);
            if (r.isEmpty()) {
                return "Operation not permitted";
            }
            Rider rider = r.get();
            if (rider.getStato() == statoRider.OCCUPATO) {
                return "You cannot accept further orders";
            }
            Ordine o = gestioneOrdini.getOrdine(id_ordine);
            if (o == null) {
                return "Operation not permitted";
            }
            String s = gestioneOrdini.updateStato(id_ordine, statoOrdine.ACCETTATO);
            rider.setStato(statoRider.OCCUPATO);
            rider.setId_ordine(id_ordine);
            riderRepository.save(rider);
            return s;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String chiudiOrdine(String id) {
        try {
            Optional<Rider> r = riderRepository.findById(id);
            Rider rider = r.get();
            if (r.isEmpty()) {
                return "Operation not permitted";
            }
            String id_ordine = rider.getId_ordine();
            rider.setId_ordine(null);
            rider.setStato(statoRider.DISPONIBILE);
            riderRepository.save(rider);
            return gestioneOrdini.updateStato(id_ordine, statoOrdine.CONSEGNATO);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String sospendiOrdine(String id) {
        try {
            Optional<Rider> r = riderRepository.findById(id);
            Rider rider = r.get();
            if (r.isEmpty()) {
                return "Operation not permitted";
            }
            return gestioneOrdini.updateStato(rider.getId_ordine(), statoOrdine.IN_SOSPESO);
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
