package com.greenteam.ordineservice.controller;

import com.greenteam.ordineservice.entity.Ordine;
import com.greenteam.ordineservice.entity.Prodotto;
import com.greenteam.ordineservice.entity.statoOrdine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.greenteam.ordineservice.service.gestioneOrdini;
import com.greenteam.ordineservice.service.gestioneRider;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Controller
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
    public String deleteOrdine(@PathVariable("id") String id, HttpServletRequest request, Model model) {
        logging(request);
        gestioneOrdini.removeOrdine(id);
        return getAll(request, model);
    }

    @RolesAllowed("rider_role")
    @GetMapping("/accept/{id_ordine}")
    public String accettaOrdine(@PathVariable("id_ordine") String id_ordine, HttpServletRequest request) {
        logging(request);
        gestioneRider.accettaOrdine(request.getRemoteUser(), id_ordine);
        return "ordineOk";
    }

    @RolesAllowed("rider_role")
    @GetMapping("/complete/{id_ordine}")
    public String chiudiOrdine(@PathVariable("id_ordine") String id_ordine, HttpServletRequest request) {
        logging(request);
        gestioneRider.chiudiOrdine(id_ordine);
        return "ordineOk";
    }

    @RolesAllowed("rider_role")
    @GetMapping("/cancel/{id}")
    public String cancellaOrdine(@PathVariable("id") String id, HttpServletRequest request) {
        logging(request);
        return gestioneOrdini.updateStato(id, statoOrdine.ANNULLATO);
    }

    @RolesAllowed("admin_role")
    @GetMapping("/suspend/{id}")
    public String sospendiOrdine(@PathVariable("id") String id, HttpServletRequest request, Model model) {
        logging(request);
        gestioneRider.sospendiOrdine(id);
        return getAll(request, model);
    }

    @RolesAllowed({"admin_role", "rider_role"})
    @GetMapping("/get/{id}")
    public Ordine getOrdine(@PathVariable("id") String id, HttpServletRequest request) {
        logging(request);
        return gestioneOrdini.getOrdine(id);
    }

    @RolesAllowed("rider_role")
    @GetMapping("/waiting/all")
    public String getWaiting(HttpServletRequest request, Model model) {
        logging(request);
        List<Ordine> ordini = gestioneOrdini.getWaiting();
        model.addAttribute("listaOrdini", ordini);
        model.addAttribute("ordine", new Ordine());
        model.addAttribute("prodotto", new Prodotto());
        return "ordiniWait";
    }

    @RolesAllowed({"admin_role"})
    @GetMapping("/all")
    public String getAll(HttpServletRequest request, Model model) {
        logging(request);
        List<Ordine> ordini = gestioneOrdini.getAll();
        model.addAttribute("listaOrdini", ordini);
        model.addAttribute("ordine", new Ordine());
        model.addAttribute("prodotto", new Prodotto());
        return "ordiniTOT";
    }

    @RolesAllowed({"user_role","admin_role"})
    @PostMapping("/createOrder")
    public void createOrder(@Valid @RequestBody LinkedList<Prodotto> l, HttpServletRequest request) {
        logging(request);
        gestioneOrdini.addToOrder(l);
        return;
    }

    @RolesAllowed("rider_role")
    @GetMapping("/rider/myOrder")
    public String myOrderRider(HttpServletRequest request, Model model) {
        logging(request);
        Ordine o = gestioneRider.myOrder(request.getRemoteUser());
        model.addAttribute("ordine", o);
        return "myOrdine";
    }
}
