package com.greenteam.ordineservice.controller;

import com.greenteam.ordineservice.entity.Ordine;
import com.greenteam.ordineservice.entity.Prodotto;
import com.greenteam.ordineservice.entity.statoOrdine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.greenteam.ordineservice.service.gestioneOrdini;
import com.greenteam.ordineservice.service.gestioneRider;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;

@Slf4j
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

    private void logging(HttpServletRequest request) {
        log.info("IP: " + request.getRemoteAddr() + " USER: " + request.getRemoteUser() + " REQUEST URL: " + request.getRequestURL() + " METHOD: " +request.getMethod());
    }

    @RolesAllowed("admin_role")
    @GetMapping("/drop/{id}")
    public String deleteOrdine(@PathVariable("id") String id, HttpServletRequest request) {
        logging(request);
        return gestioneOrdini.removeOrdine(id);
    }

    @RolesAllowed("rider_role")
    @GetMapping("/accept/{id_rider}/{id_ordine}")
    public String accettaOrdine(@PathVariable("id_rider") String id_rider, @PathVariable("id_ordine") String id_ordine, HttpServletRequest request) {
        logging(request);
        return gestioneRider.accettaOrdine(id_rider, id_ordine);
    }

    @RolesAllowed("rider_role")
    @GetMapping("/complete/{id_rider}")
    public String chiudiOrdine(@PathVariable("id_rider") String id_rider, HttpServletRequest request) {
        logging(request);
        return gestioneRider.chiudiOrdine(id_rider);
    }

    @RolesAllowed("rider_role")
    @GetMapping("/cancel/{id}")
    public String cancellaOrdine(@PathVariable("id") String id, HttpServletRequest request) {
        logging(request);
        return gestioneOrdini.updateStato(id, statoOrdine.ANNULLATO);
    }

    @RolesAllowed("admin_role")
    @GetMapping("/suspend/{id}")
    public String sospendiOrdine(@PathVariable("id") String id, HttpServletRequest request) {
        logging(request);
        return gestioneRider.sospendiOrdine(id);
    }

    @RolesAllowed({"admin_role", "rider_role"})
    @GetMapping("/get/{id}")
    public Ordine getOrdine(@PathVariable("id") String id, HttpServletRequest request) {
        logging(request);
        return gestioneOrdini.getOrdine(id);
    }

    @RolesAllowed({"admin_role", "rider_role"})
    @GetMapping("/all")
    public List<Ordine> getAll(HttpServletRequest request) {
        logging(request);
        return gestioneOrdini.getAll();
    }

    @RolesAllowed({"user_role","admin_role"})
    @PostMapping("/createOrder")
    public <Any> Any createOrder(@Valid @RequestBody LinkedList<Prodotto> l, HttpServletRequest request) {
        logging(request);
        return gestioneOrdini.addToOrder(l);
    }
}
