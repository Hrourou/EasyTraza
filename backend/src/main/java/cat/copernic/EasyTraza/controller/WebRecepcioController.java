package cat.copernic.EasyTraza.controller;

import cat.copernic.EasyTraza.business.MateriaPrimeraService;
import cat.copernic.EasyTraza.business.OcrService;
import cat.copernic.EasyTraza.business.ProveidorService;
import cat.copernic.EasyTraza.business.RecepcioService;
import cat.copernic.EasyTraza.dto.AlbaraLotFormDTO;
import cat.copernic.EasyTraza.dto.LotFormDTO;
import cat.copernic.EasyTraza.validation.Validator;
import cat.copernic.EasyTraza.business.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;

/**
 *
 * @author HAMZA
 */
@Controller
@RequestMapping("/recepcio")
public class WebRecepcioController {

    @Autowired private OcrService ocrService;
    @Autowired private RecepcioService recepcioService;
    @Autowired private ProveidorService proveidorService;
    @Autowired private MateriaPrimeraService materiaPrimeraService;
    @Autowired private FileStorageService fileStorageService;

    @GetMapping
    public String veurePantallaRecepcio(Model model) {
        model.addAttribute("proveidors", proveidorService.getAllProveidors());
        model.addAttribute("materies", materiaPrimeraService.getAllMateries());
        
        AlbaraLotFormDTO dto = new AlbaraLotFormDTO();
        dto.setDataRecepcio(LocalDate.now()); 
        
        LotFormDTO filaNeta = new LotFormDTO();
        dto.getLiniesLote().add(filaNeta);
        
        model.addAttribute("recepcioDTO", dto);
        return "albaraproveidor"; 
    }

    @PostMapping("/save")
    public String guardarAlbara(@ModelAttribute("recepcioDTO") AlbaraLotFormDTO dto, Model model) {
        try {
            // 1. Validem el CIF abans de fer res
            if (dto.getCifProveidor() != null && !dto.getCifProveidor().isEmpty()) {
                if (!Validator.validarNifCif(dto.getCifProveidor())) {
                    throw new RuntimeException("El CIF o NIF introduït no té un format vàlid.");
                }
            }

            // 2. Guardem l'albarà
            recepcioService.guardarRecepcio(dto); 
            return "redirect:/recepcio?success=true"; 

        } catch (DataIntegrityViolationException e) {
            // 3. Captura l'error de Base de dades quan el lot ja existeix
            model.addAttribute("error", "Error: Aquest albarà o lot ja ha estat escanejat i registrat anteriorment en el sistema.");
            return recargarVistaError(model, dto);
        } catch (Exception e) {
            // Captura qualsevol altre error genèric
            String msg = e.getMessage();
            if(msg != null && msg.toLowerCase().contains("duplicate")) {
                msg = "Aquest albarà o lot ja ha estat escanejat i registrat anteriorment.";
            }
            model.addAttribute("error", "Error al guardar: " + msg);
            return recargarVistaError(model, dto);
        }
    }

    @PostMapping("/ocr")
    @ResponseBody
    public ResponseEntity<AlbaraLotFormDTO> processarImatgeOCR(@RequestParam("file") MultipartFile file) {
        String fotoUrl = fileStorageService.saveAlbaranImage(file);
        AlbaraLotFormDTO resultatOCR = ocrService.llegirAlbara(file);
        resultatOCR.setFotoAlbaraUrl(fotoUrl);
        return ResponseEntity.ok(resultatOCR);
    }

    // Mètode privat per mantenir el codi net
    private String recargarVistaError(Model model, AlbaraLotFormDTO dto) {
        model.addAttribute("proveidors", proveidorService.getAllProveidors());
        model.addAttribute("materies", materiaPrimeraService.getAllMateries());
        model.addAttribute("recepcioDTO", dto);
        return "albaraproveidor";
    }
}