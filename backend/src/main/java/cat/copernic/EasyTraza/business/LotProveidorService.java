/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.business;

import cat.copernic.EasyTraza.entities.LotProveidor;
import cat.copernic.EasyTraza.entities.LotProveidorId;
import cat.copernic.EasyTraza.entities.Usuari;
import cat.copernic.EasyTraza.enums.EstatLot; // <-- IMPORT CORREGIDO
import cat.copernic.EasyTraza.repository.LotProveidorRepository;
import cat.copernic.EasyTraza.repository.UsuariRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
/**
 *
 * @author HAMZA
 */
@Service
public class LotProveidorService {

    @Autowired
    private LotProveidorRepository lotRepo;

    @Autowired
    private UsuariRepository usuariRepo;

    /**
     * NOU MÈTODE PUBLIC: Retorna els lots segons el seu estat
     */
    public List<LotProveidor> obtenirLotsPerEstat(EstatLot estat) {
        return lotRepo.findByEstat(estat);
    }

    /**
     * Comprova si hi ha un lot obert d'una matèria primera específica.
     */
    public LotProveidor comprovarLotObertMateria(Long idMateria) {
        return lotRepo.findByEstat(EstatLot.OBERT).stream()
                .filter(l -> l.getMateriaPrimera().getId().equals(idMateria))
                .findFirst()
                .orElse(null);
    }

    /**
     * Inicia un lot i el posa en estat OBERT.
     */
    @Transactional
    public void iniciarLot(String proveidorCif, String idLot, boolean confirmarTancamentAnterior, String nifUsuari) { // <-- CAMBIADO A STRING
        LotProveidor lotNou = lotRepo.findById(new LotProveidorId(idLot, proveidorCif))
                .orElseThrow(() -> new RuntimeException("Lot no trobat"));

        Usuari operariInici = null;
        if (nifUsuari != null && !nifUsuari.isEmpty()) {
            if (nifUsuari.contains("@")) {
                operariInici = usuariRepo.findByEmail(nifUsuari).orElse(null);
            } else {
                operariInici = usuariRepo.findById(nifUsuari).orElse(null);
            }
        }

        // Comprovem si ja hi ha un lot obert d'aquesta matèria primera
        LotProveidor obertAnterior = comprovarLotObertMateria(lotNou.getMateriaPrimera().getId());

        if (obertAnterior != null) {
            if (!confirmarTancamentAnterior) {
                // Si n'hi ha un i l'usuari no ha confirmat, enviem un avís al mòbil
                throw new IllegalStateException("EXISTEIX_LOT_OBERT");
            } else {
                // Si confirma, tanquem el lot anterior
                obertAnterior.setEstat(EstatLot.ACABAT);
                obertAnterior.setDataAcabament(LocalDateTime.now());
                obertAnterior.setUsuariFinalitzador(operariInici);
                lotRepo.save(obertAnterior);
            }
        }

        lotNou.setEstat(EstatLot.OBERT);
        lotNou.setDataObertura(LocalDateTime.now());
        lotNou.setUsuariIniciador(operariInici);
        lotRepo.save(lotNou);
    }

    /**
     * Finalitza un lot i el posa en estat ACABAT.
     */
    @Transactional
    public void finalitzarLot(String proveidorCif, String idLot, String nifUsuari) { // <-- CAMBIADO A STRING
        LotProveidor lot = lotRepo.findById(new LotProveidorId(idLot, proveidorCif))
                .orElseThrow(() -> new RuntimeException("Lot no trobat"));

        Usuari operariFi = null;
        if (nifUsuari != null && !nifUsuari.isEmpty()) {
            if (nifUsuari.contains("@")) {
                operariFi = usuariRepo.findByEmail(nifUsuari).orElse(null);
            } else {
                operariFi = usuariRepo.findById(nifUsuari).orElse(null);
            }
        }

        lot.setEstat(EstatLot.ACABAT);
        lot.setDataAcabament(LocalDateTime.now());
        lot.setUsuariFinalitzador(operariFi);
        lotRepo.save(lot);
    }
}