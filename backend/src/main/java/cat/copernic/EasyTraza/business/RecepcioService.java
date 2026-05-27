/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.business;

import cat.copernic.EasyTraza.dto.AlbaraLotFormDTO;
import cat.copernic.EasyTraza.dto.LotFormDTO;
import cat.copernic.EasyTraza.entities.*;
import cat.copernic.EasyTraza.enums.EstatLot;
import cat.copernic.EasyTraza.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.Optional;

/**
 *
 * @author HAMZA
 */
@Service
public class RecepcioService {

    @Autowired private AlbaraProveidorRepository albaraRepo;
    @Autowired private ProveidorRepository proveidorRepo;
    @Autowired private MateriaPrimeraRepository materiaRepo;
    @Autowired private LotProveidorRepository lotRepo;
    

@Transactional
    public void guardarRecepcio(AlbaraLotFormDTO dto) {
        
        // La comprobación de duplicados que lanzaba un error se ha eliminado para permitir
        // que lotes con el mismo ID pero distinto proveedor coexistan, o si es el mismo
        // proveedor, que se sumen sus cantidades.
        
        // --- 1. LIMPIEZA DE SEGURIDAD DEL CIF ---
        String cifTemporal = dto.getCifProveidor() != null ? dto.getCifProveidor().trim() : "DESCONOCIDO";
        // Aquí hacemos el recorte y lo guardamos en una variable FINAL para que la Lambda de Java no se queje
        final String cifLimpio = cifTemporal.length() > 15 ? cifTemporal.substring(0, 15) : cifTemporal;

        // --- 2. BUSCAR O AUTOCREAR PROVEEDOR ---
        Proveidor prov = proveidorRepo.findById(cifLimpio).orElseGet(() -> {
            Proveidor nuevoProv = new Proveidor();
            nuevoProv.setCif(cifLimpio);
            
            // Si el OCR ha detectado un nombre, lo usamos. Si no, ponemos uno por defecto.
            if (dto.getNomProveidor() != null && !dto.getNomProveidor().trim().isEmpty()) {
                nuevoProv.setNom(dto.getNomProveidor());
            } else {
                nuevoProv.setNom("PROVEEDOR AUTOCREADO (" + cifLimpio + ")");
            }
            
            return proveidorRepo.save(nuevoProv);
        });

        // Creamos el Albarán
        AlbaraProveidor albara = new AlbaraProveidor();
        albara.setProveidor(prov);
        albara.setDataRecepcio(dto.getDataRecepcio());
        if (dto.getFotoAlbaraUrl() != null && !dto.getFotoAlbaraUrl().isEmpty()) {
            albara.setFotoAlbaraUrl(dto.getFotoAlbaraUrl());
        }

        // Recorremos todas las filas (lotes) que ha enviado el HTML/Móvil
        for (LotFormDTO lineaDTO : dto.getLiniesLote()) {
            
            // Si la línea está vacía, la saltamos
            if (lineaDTO.getIdLot() == null || lineaDTO.getIdLot().isEmpty()) continue;

            MateriaPrimera mp;

            // CASO A: El usuario seleccionó la Materia Prima del desplegable manualmente
            if (lineaDTO.getMateriaPrimeraId() != null) {
                mp = materiaRepo.findById(lineaDTO.getMateriaPrimeraId())
                        .orElseThrow(() -> new RuntimeException("Materia Prima seleccionada no encontrada"));
            } 
            // CASO B: Viene del OCR -> ¡AUTOCREACIÓN!
            else if (lineaDTO.getNomMateriaPrimeraDetectada() != null && !lineaDTO.getNomMateriaPrimeraDetectada().isEmpty()) {
                Optional<MateriaPrimera> mpExistente = materiaRepo.findByNom(lineaDTO.getNomMateriaPrimeraDetectada());
                
                if (mpExistente.isPresent()) {
                    mp = mpExistente.get();
                } else {
                    // La creamos nueva sobre la marcha
                    mp = new MateriaPrimera();
                    mp.setNom(lineaDTO.getNomMateriaPrimeraDetectada());
                    mp.setDescripcio("OCR - " + lineaDTO.getNomMateriaPrimeraDetectada());
                    mp = materiaRepo.save(mp); 
                }
            } else {
                throw new RuntimeException("Debe seleccionar una Materia Prima o usar el OCR para el lote: " + lineaDTO.getIdLot());
            }

            // --- 3. LÓGICA DE LOTES REPETIDOS O NUEVOS ---
            Optional<LotProveidor> lotExistent = lotRepo.findById(new LotProveidorId(lineaDTO.getIdLot(), cifLimpio));
            LotProveidor lot;

            if (lotExistent.isPresent()) {
                // Si el lote ya existe (llegó otro día), verificamos que la materia prima sea la misma
                lot = lotExistent.get();
                if (!lot.getMateriaPrimera().getId().equals(mp.getId())) {
                    throw new RuntimeException("El lote '" + lineaDTO.getIdLot() + "' ya existe para este proveedor pero con una materia prima diferente (" + lot.getMateriaPrimera().getNom() + "). Un mismo lote no puede contener diferentes materias primas.");
                }
                lot.setUnitats(lot.getUnitats() + lineaDTO.getUnitats());
            } else {
                // Si es un lote totalmente nuevo, lo creamos desde cero
                lot = new LotProveidor();
                lot.setIdLot(lineaDTO.getIdLot());
                lot.setProveidorCif(cifLimpio);
                lot.setUnitats(lineaDTO.getUnitats());
                lot.setQuantitat(0.0);
                lot.setMateriaPrimera(mp);
                lot.setEstat(EstatLot.EN_ESTOC);
            }

            albara.addLot(lot);
        }

        // Guardamos todo de golpe
        albaraRepo.save(albara); 
    }
}