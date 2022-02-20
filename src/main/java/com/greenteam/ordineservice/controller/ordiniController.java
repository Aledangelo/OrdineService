package com.greenteam.ordineservice.controller;

import com.greenteam.ordineservice.entity.Ordine;
import com.greenteam.ordineservice.entity.Prodotto;
import com.greenteam.ordineservice.entity.statoOrdine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.greenteam.ordineservice.service.gestioneOrdini;
import com.greenteam.ordineservice.service.gestioneRider;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
        try {
            gestioneOrdini.removeOrdine(id);
            return getAll(request, model);
        } catch (Exception e) {
            return "pagina_inaccessibile";
        }
    }

    @RolesAllowed("rider_role")
    @GetMapping("/accept/{id_ordine}")
    public String accettaOrdine(@PathVariable("id_ordine") String id_ordine, HttpServletRequest request) {
        logging(request);
        try {
            gestioneRider.accettaOrdine(request.getRemoteUser(), id_ordine);
            return "ordineOk";
        } catch (Exception e) {
            return "pagina_inaccessibile";
        }
    }

    @RolesAllowed("rider_role")
    @GetMapping("/complete/{id_ordine}")
    public String chiudiOrdine(@PathVariable("id_ordine") String id_ordine, HttpServletRequest request) {
        logging(request);
        try {
            gestioneRider.chiudiOrdine(id_ordine, request.getRemoteUser());
            return "ordineOk";
        } catch (Exception e) {
            return "pagina_inaccessibile";
        }
    }

    @RolesAllowed("rider_role")
    @GetMapping("/cancel/{id}")
    public String cancellaOrdine(@PathVariable("id") String id, HttpServletRequest request) {
        logging(request);
        try {
            return gestioneOrdini.updateStato(id, statoOrdine.ANNULLATO);
        } catch (Exception e) {
            return null;
        }
    }

    @RolesAllowed("admin_role")
    @GetMapping("/suspend/{id}")
    public String sospendiOrdine(@PathVariable("id") String id, HttpServletRequest request, Model model) {
        logging(request);
        try {
            gestioneRider.sospendiOrdine(id);
            return getAll(request, model);
        } catch (Exception e) {
            return "pagina_inaccessibile";
        }
    }

    @RolesAllowed({"admin_role", "rider_role"})
    @GetMapping("/get/{id}")
    public String getOrdine(@PathVariable("id") String id, HttpServletRequest request) {
        logging(request);
        try {
            gestioneOrdini.getOrdine(id);
            return "";
        } catch (Exception e) {
            return "pagina_inaccessibile";
        }
    }

    @RolesAllowed("rider_role")
    @GetMapping("/waiting/all")
    public String getWaiting(HttpServletRequest request, Model model) {
        logging(request);
        try {
            List<Ordine> ordini = gestioneOrdini.getWaiting();
            model.addAttribute("listaOrdini", ordini);
            model.addAttribute("ordine", new Ordine());
            model.addAttribute("prodotto", new Prodotto());
            return "ordiniWait";
        } catch (Exception e) {
            return "pagina_inaccessibile";
        }
    }

    @RolesAllowed({"admin_role"})
    @GetMapping("/all")
    public String getAll(HttpServletRequest request, Model model) {
        logging(request);
        try {
            List<Ordine> ordini = gestioneOrdini.getAll();
            model.addAttribute("ATTESA", statoOrdine.IN_ATTESA);
            model.addAttribute("SOSPESO", statoOrdine.IN_SOSPESO);
            model.addAttribute("listaOrdini", ordini);
            model.addAttribute("ordine", new Ordine());
            model.addAttribute("prodotto", new Prodotto());
            return "ordiniTOT";
        } catch (Exception e) {
            return "pagina_inaccessibile";
        }
    }

    @RolesAllowed({"user_role","admin_role"})
    @PostMapping("/createOrder/{id_user}/{address}")
    public String createOrder(@Valid @RequestBody LinkedList<Prodotto> l, HttpServletRequest request, @PathVariable("id_user") String id_user, @PathVariable("address")  String address) {
        logging(request);
        try {
            gestioneOrdini.addToOrder(l, id_user, address);
            return "ordineOk";
        } catch (Exception e) {
            return "pagina_inaccessibile";
        }
    }

    @RolesAllowed("admin_role")
    @GetMapping("/activate/{id}")
    public String reactivate(@PathVariable("id") String id_ordine, HttpServletRequest request, Model model) {
        logging(request);
        try {
            gestioneRider.riattivaOrdine(id_ordine);
            return getAll(request, model);
        } catch (Exception e) {
            return "pagina_inaccessibile";
        }
    }

    @RolesAllowed("rider_role")
    @GetMapping("/rider/myOrder")
    public String myOrderRider(HttpServletRequest request, Model model) {
        logging(request);
        try {
            List<Ordine> o = gestioneRider.myOrderRider(request.getRemoteUser());
            model.addAttribute("ordine", new Ordine());
            model.addAttribute("listaOrdini", o);
            model.addAttribute("CONSEGNATO", statoOrdine.CONSEGNATO);
            return "myOrdine";
        } catch (Exception e) {
            return "pagina_inaccessibile";
        }
    }

    @RolesAllowed("user_role")
    @GetMapping("/user/myOrder")
    public String myOrderUser(HttpServletRequest request, Model model) {
        logging(request);
        try {
            List<Ordine> o = gestioneRider.myOrderUser(request.getRemoteUser());
            model.addAttribute("ordine", new Ordine());
            model.addAttribute("listaOrdini", o);
            return "myOrdineUser";
        } catch (Exception e) {
            return "pagina_inaccessibile";
        }
    }
}
