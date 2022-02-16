package com.greenteam.ordineservice.service;

import com.greenteam.ordineservice.entity.Ordine;
import com.greenteam.ordineservice.entity.Rider;
import com.greenteam.ordineservice.entity.statoOrdine;
import com.greenteam.ordineservice.entity.statoRider;
import com.greenteam.ordineservice.repository.riderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Service
public class gestioneRider {
    @Autowired
    private riderRepository riderRepository;

    @Autowired
    private gestioneOrdini gestioneOrdini;

    public String accettaOrdine(String id_rider, String id_ordine) {
        try {
            Ordine o = gestioneOrdini.getOrdine(id_ordine);
            if (o == null) {
                return "Operation not permitted";
            }
            o.setNome_rider(id_rider);
            gestioneOrdini.setRiderOrdine(id_rider, id_ordine);
            String s = gestioneOrdini.updateStato(id_ordine, statoOrdine.ACCETTATO);
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
            return gestioneOrdini.updateStato(id, statoOrdine.IN_SOSPESO);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public Ordine myOrder(String id_rider) {
        List<Ordine> l = gestioneOrdini.getAll();
        for (Ordine o: l) {
            if (o.getNome_rider() != null && o.getNome_rider().equals(id_rider)) {
                return o;
            }
        }
        return null;
    }
}
