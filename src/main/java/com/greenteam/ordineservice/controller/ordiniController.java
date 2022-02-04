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

    @RolesAllowed("admin_role")
    @GetMapping("/drop/{id}")
    public String deleteOrdine(@PathVariable("id") String id) { return gestioneOrdini.removeOrdine(id); }

    @RolesAllowed("rider_role")
    @GetMapping("/accept/{id_rider}/{id_ordine}")
    public String accettaOrdine(@PathVariable("id_rider") String id_rider, @PathVariable("id_ordine") String id_ordine) {
        return gestioneRider.accettaOrdine(id_rider, id_ordine);
    }

    @RolesAllowed("rider_role")
    @GetMapping("/complete/{id_rider}")
    public String chiudiOrdine(@PathVariable("id_rider") String id_rider) { return gestioneRider.chiudiOrdine(id_rider); }

    @RolesAllowed("admin_role")
    @GetMapping("/cancel/{id}")
    public String cancellaOrdine(@PathVariable("id") String id) { return gestioneOrdini.updateStato(id, statoOrdine.ANNULLATO); }

    @RolesAllowed("admin_role")
    @GetMapping("/suspend/{id}")
    public String sospendiOrdine(@PathVariable("id") String id) { return gestioneRider.sospendiOrdine(id); }

    @RolesAllowed({"admin_role", "rider_role"})
    @GetMapping("/get/{id}")
    public Ordine getOrdine(@PathVariable("id") String id) { return gestioneOrdini.getOrdine(id); }

    @RolesAllowed("admin_role")
    @GetMapping("/all")
    public List<Ordine> getAll() { return gestioneOrdini.getAll(); }

    @RolesAllowed("user_role")
    @PostMapping("/createOrder")
    public <Any> Any createOrder(@Valid @RequestBody LinkedList<Prodotto> l) { return gestioneOrdini.addToOrder(l); }
}
