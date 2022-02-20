package com.greenteam.ordineservice.service;

import com.greenteam.ordineservice.entity.Ordine;
import com.greenteam.ordineservice.entity.statoOrdine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class gestioneRider {

    @Autowired
    private gestioneOrdini gestioneOrdini;

    public void accettaOrdine(String id_rider, String id_ordine) throws Exception {
        Ordine o = gestioneOrdini.getOrdine(id_ordine);
        if (o == null) {
            return;
        }
        if (o.getStato() != statoOrdine.IN_ATTESA) {
            return;
        }
        o.setNome_rider(id_rider);
        gestioneOrdini.setRiderOrdine(id_rider, id_ordine);
        gestioneOrdini.updateStato(id_ordine, statoOrdine.ACCETTATO);
    }

    public void chiudiOrdine(String id_ordine, String id_rider) throws Exception {
        Ordine o = gestioneOrdini.getOrdine(id_ordine);
        if (o.getNome_rider().equals(id_rider)) {
            gestioneOrdini.updateStato(id_ordine, statoOrdine.CONSEGNATO);
        }
    }

    public void sospendiOrdine(String id) throws Exception{
        Ordine o = gestioneOrdini.getOrdine(id);
        if (o.getStato() != statoOrdine.IN_ATTESA) {
            return;
        }
        gestioneOrdini.updateStato(id, statoOrdine.IN_SOSPESO);
    }

    public void riattivaOrdine(String id) throws Exception{
        Ordine o = gestioneOrdini.getOrdine(id);
        if (o.getStato() != statoOrdine.IN_SOSPESO) {
            return;
        }
        gestioneOrdini.updateStato(id, statoOrdine.IN_ATTESA);
    }

    public List<Ordine> myOrderRider(String id_rider) throws Exception{
        List<Ordine> l = gestioneOrdini.getAll();
        List<Ordine> myList = new ArrayList<Ordine>();
        for (Ordine o: l) {
            if (o.getNome_rider() != null && o.getNome_rider().equals(id_rider)) {
                myList.add(o);
            }
        }
        return myList;
    }

    public List<Ordine> myOrderUser(String id_user) throws Exception{
        List<Ordine> l = gestioneOrdini.getAll();
        List<Ordine> myList = new ArrayList<Ordine>();
        for (Ordine o: l) {
            if (o.getId_user() != null && o.getId_user().equals(id_user)) {
                myList.add(o);
            }
        }
        return myList;
    }
}
