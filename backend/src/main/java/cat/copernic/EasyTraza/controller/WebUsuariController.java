package cat.copernic.EasyTraza.controller;

import cat.copernic.EasyTraza.business.UsuariService;
import cat.copernic.EasyTraza.dto.UsuariDTO;
import cat.copernic.EasyTraza.enums.UserRole;
import cat.copernic.EasyTraza.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/usuaris")
public class WebUsuariController {

    @Autowired
    private UsuariService usuariService;

    // 1. Mostrar la lista (GET /admin/usuaris)
    @GetMapping
    public String llistatUsuaris(Model model) {
        model.addAttribute("usuaris", usuariService.getAllUsuaris());
        model.addAttribute("usuariNou", new UsuariDTO()); 
        model.addAttribute("roles", UserRole.values());
        model.addAttribute("hasErrors", false); // Por defecto no hay errores
        return "usuaris";
    }

    // 2. Guardar nuevo usuario (POST /admin/usuaris/save)
    @PostMapping("/save")
    public String guardarNouUsuari(@ModelAttribute("usuariNou") UsuariDTO dto, Model model) {
        
        // --- VALIDACIONES ---
        if (!Validator.validarNifCif(dto.getDni())) {
            model.addAttribute("error", "Error: El formato del DNI/CIF no es válido.");
            return recargarVistaConError(model, dto, "create"); // Indicamos que el error viene de crear
        }

        if (!Validator.validarEmail(dto.getEmail())) {
            model.addAttribute("error", "Error: El formato del correo electrónico no es válido.");
            return recargarVistaConError(model, dto, "create");
        }
        
        if (dto.getTelefon() != null && !dto.getTelefon().isEmpty() && !Validator.validarTelefon(dto.getTelefon())) {
            model.addAttribute("error", "Error: El teléfono introducido no es válido (deben ser 9 dígitos).");
            return recargarVistaConError(model, dto, "create");
        }

        if (dto.getPassword() == null || dto.getPassword().length() < 8) {
            model.addAttribute("error", "Error: La contraseña debe tener al menos 8 caracteres.");
            return recargarVistaConError(model, dto, "create");
        }

        try {
            usuariService.createUsuari(dto);
            return "redirect:/admin/usuaris";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return recargarVistaConError(model, dto, "create");
        }
    }

    // 3. Actualizar usuario existente (POST /admin/usuaris/update)
    @PostMapping("/update")
    public String actualizarUsuari(@ModelAttribute("usuariNou") UsuariDTO dto, Model model) {
        
        if (!Validator.validarEmail(dto.getEmail())) {
            model.addAttribute("error", "Error: El formato del correo electrónico no es válido.");
            return recargarVistaConError(model, dto, "edit"); // Indicamos que el error viene de editar
        }

        if (dto.getTelefon() != null && !dto.getTelefon().isEmpty() && !Validator.validarTelefon(dto.getTelefon())) {
            model.addAttribute("error", "Error: El teléfono introducido no es válido.");
            return recargarVistaConError(model, dto, "edit");
        }

        if (dto.getPassword() != null && !dto.getPassword().isEmpty() && dto.getPassword().length() < 8) {
            model.addAttribute("error", "Error: La nueva contraseña debe tener al menos 8 caracteres.");
            return recargarVistaConError(model, dto, "edit");
        }

        try {
            String adminEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            usuariService.updateUsuari(dto.getDni(), dto, adminEmail); 
            return "redirect:/admin/usuaris";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return recargarVistaConError(model, dto, "edit");
        }
    }

    // 4. Eliminar usuario (POST /admin/usuaris/delete)
    @PostMapping("/delete")
    public String eliminarUsuari(@RequestParam("dni") String dni, Model model) { 
        try {
            String adminEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            usuariService.deleteUsuari(dni, adminEmail);
            return "redirect:/admin/usuaris";
        } catch (RuntimeException e) {
            // ¡CAMBIO CLAVE!: Usamos "globalError" en lugar de "error"
            model.addAttribute("globalError", e.getMessage());
            return recargarVistaConError(model, new UsuariDTO(), "delete");
        }
    }
    
    // Método modificado para incluir el flag hasErrors y el modeError
    private String recargarVistaConError(Model model, UsuariDTO dto, String modeError) {
        model.addAttribute("usuaris", usuariService.getAllUsuaris());
        model.addAttribute("usuariNou", dto);
        model.addAttribute("roles", UserRole.values());
        model.addAttribute("hasErrors", true); // Flag para abrir el modal
        model.addAttribute("modeError", modeError); // Para saber si abrir modal de crear o editar
        return "usuaris";
    }
}