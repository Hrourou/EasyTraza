package cat.copernic.EasyTraza.business;

import cat.copernic.EasyTraza.dto.UsuariDTO;
import cat.copernic.EasyTraza.entities.Usuari;
import cat.copernic.EasyTraza.enums.UserRole;
import cat.copernic.EasyTraza.repository.UsuariRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuariService {

    @Autowired
    private UsuariRepository usuariRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private UsuariDTO mapToDTO(Usuari u) {
        UsuariDTO dto = new UsuariDTO();
        dto.setEmail(u.getEmail());
        dto.setNom(u.getNom());
        dto.setCognom(u.getCognom());
        dto.setDni(u.getDni());
        dto.setDireccio(u.getDireccio());
        dto.setTelefon(u.getTelefon());
        dto.setPassword(u.getPassword());
        dto.setRol(u.getRol());
        dto.setFotoPerfilUrl(u.getFotoPerfilUrl());
        return dto;
    }

    private Usuari mapToEntity(UsuariDTO dto) {
        Usuari u = new Usuari(dto.getEmail(), dto.getNom(), dto.getCognom(), dto.getDni(), 
                dto.getDireccio(), dto.getTelefon(), dto.getPassword(), dto.getRol());
        u.setFotoPerfilUrl(dto.getFotoPerfilUrl());
        return u;
    }

    public List<UsuariDTO> getAllUsuaris() {
        return usuariRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public UsuariDTO getUsuariByDni(String dni) {
        return usuariRepository.findById(dni).map(this::mapToDTO).orElse(null);
    }
    public UsuariDTO getUsuariByEmail(String email) {
    // Buscamos en el repositorio y lo convertimos a DTO usando tu método mapToDTO
    return usuariRepository.findByEmail(email)
            .map(this::mapToDTO)
            .orElse(null);
}

    public UsuariDTO createUsuari(UsuariDTO dto) {
        if (usuariRepository.existsById(dto.getDni())) {
            throw new RuntimeException("El DNI ja està registrat.");
        }
        if (usuariRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("L'email ja està registrat.");
        }
        
        // Solo asignamos TREBALLADOR por defecto si no se ha seleccionado un rol
        if (dto.getRol() == null) {
            dto.setRol(UserRole.TREBALLADOR);
        }

        // APLICAMOS EL HASH A LA CONTRASEÑA NUEVA
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            String hashedPassword = passwordEncoder.encode(dto.getPassword());
            dto.setPassword(hashedPassword);
        }

        Usuari saved = usuariRepository.save(mapToEntity(dto));
        return mapToDTO(saved);
    }

    public UsuariDTO updateUsuari(String dni, UsuariDTO dto, String adminActualEmail) {
        Usuari existent = usuariRepository.findById(dni)
                .orElseThrow(() -> new RuntimeException("Usuari no trobat"));

        if (!existent.getEmail().equals(dto.getEmail()) && usuariRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("El nou email ja està en ús per un altre usuari.");
        }

        if (existent.getEmail().equals(adminActualEmail) && existent.getRol() != dto.getRol()) {
            throw new RuntimeException("No pots modificar el teu propi rol.");
        }

        if (existent.getRol() == UserRole.ADMIN && dto.getRol() == UserRole.TREBALLADOR) {
            long adminCount = usuariRepository.countByRol(UserRole.ADMIN);
            if (adminCount <= 1) {
                throw new RuntimeException("No pots degradar a l'últim ADMIN del sistema.");
            }
        }

        existent.setEmail(dto.getEmail());
        existent.setNom(dto.getNom());
        existent.setCognom(dto.getCognom());
        existent.setDireccio(dto.getDireccio());
        existent.setTelefon(dto.getTelefon());
        
        // APLICAMOS EL HASH SI EL USUARIO HA ESCRITO UNA NUEVA CONTRASEÑA
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            String hashedPassword = passwordEncoder.encode(dto.getPassword());
            existent.setPassword(hashedPassword);
        }
        
        existent.setRol(dto.getRol());
        existent.setFotoPerfilUrl(dto.getFotoPerfilUrl());

        Usuari updated = usuariRepository.save(existent);
        return mapToDTO(updated);
    }

    public void deleteUsuari(String dni, String adminActualEmail) {
        Usuari existent = usuariRepository.findById(dni)
                .orElseThrow(() -> new RuntimeException("Usuari no trobat"));

        if (existent.getEmail().equals(adminActualEmail)) {
            throw new RuntimeException("No pots eliminar-te a tu mateix.");
        }

        if (existent.getRol() == UserRole.ADMIN) {
            long adminCount = usuariRepository.countByRol(UserRole.ADMIN);
            if (adminCount <= 1) {
                throw new RuntimeException("No pots eliminar a l'últim ADMIN del sistema.");
            }
        }

        usuariRepository.deleteById(dni);
    }
}