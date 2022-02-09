package com.greenteam.ordineservice.controller;

import com.greenteam.ordineservice.entity.Ordine;
import com.greenteam.ordineservice.entity.Prodotto;
import com.greenteam.ordineservice.entity.statoOrdine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.greenteam.ordineservice.service.gestioneOrdini;
import com.greenteam.ordineservice.service.gestioneRider;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/ordini")
public class ordiniController {

    @Autowired
    private gestioneOrdini gestioneOrdini;

    @Autowired
    private gestioneRider gestioneRider;

    /*
    @PostMapping("/insert")
    public Ordine saveOrdine(@RequestBody Ordine o) {return gestioneOrdini.saveOrdine(o); }
     */

    @RolesAllowed("Admin")
    @GetMapping("/drop/{id}")
    public String deleteOrdine(@PathVariable("id") String id) { return gestioneOrdini.removeOrdine(id); }

    @RolesAllowed("Rider")
    @GetMapping("/accept/{id_rider}/{id_ordine}")
    public String accettaOrdine(@PathVariable("id_rider") String id_rider, @PathVariable("id_ordine") String id_ordine) {
        return gestioneRider.accettaOrdine(id_rider, id_ordine);
    }

    @RolesAllowed("Rider")
    @GetMapping("/complete/{id_rider}")
    public String chiudiOrdine(@PathVariable("id_rider") String id_rider) { return gestioneRider.chiudiOrdine(id_rider); }

    @RolesAllowed("Rider")
    @GetMapping("/cancel/{id}")
    public String cancellaOrdine(@PathVariable("id") String id) { return gestioneOrdini.updateStato(id, statoOrdine.ANNULLATO); }

    @RolesAllowed("Admin")
    @GetMapping("/suspend/{id}")
    public String sospendiOrdine(@PathVariable("id") String id) { return gestioneRider.sospendiOrdine(id); }

    @RolesAllowed({"Admin", "Rider"})
    @GetMapping("/get/{id}")
    public Ordine getOrdine(@PathVariable("id") String id) { return gestioneOrdini.getOrdine(id); }

    @RolesAllowed({"admin_role", "rider_role"})
    @GetMapping("/all")
    public List<Ordine> getAll() { return gestioneOrdini.getAll(); }

    @RolesAllowed("User")
    @PostMapping("/createOrder")
    public <Any> Any createOrder(@Valid @RequestBody LinkedList<Prodotto> l) { return gestioneOrdini.addToOrder(l); }
}
