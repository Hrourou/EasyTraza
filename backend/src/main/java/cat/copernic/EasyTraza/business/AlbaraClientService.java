package cat.copernic.EasyTraza.business;

import cat.copernic.EasyTraza.dto.AlbaraClientFormDTO;
import cat.copernic.EasyTraza.dto.LiniaClientDTO;
import cat.copernic.EasyTraza.entities.*;
import cat.copernic.EasyTraza.enums.EstatAlbaraClient;
import cat.copernic.EasyTraza.enums.EstatLiniaClient;
import cat.copernic.EasyTraza.repository.AlbaraClientRepository;
import cat.copernic.EasyTraza.repository.ClientRepository;
import cat.copernic.EasyTraza.repository.ProducteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime; 
import java.util.List;
import java.util.Optional;

/**
 *
 * @author HAMZA
 */
@Service
public class AlbaraClientService {

    @Autowired private AlbaraClientRepository albaraRepo;
    @Autowired private ClientRepository clientRepo;
    @Autowired private ProducteRepository producteRepo;

    public List<AlbaraClient> getAllAlbarans() {
        return albaraRepo.findAll();
    }

    // ARREGLADO: Ahora recibe LocalDateTime y busca ignorando nanosegundos para evitar fallos de serialización de Thymeleaf
    public AlbaraClient getAlbaraById(String nif, LocalDateTime data) {
        LocalDateTime baseData = data.withNano(0);
        LocalDateTime nextSecond = baseData.plusSeconds(1);
        
        return albaraRepo.findByNifAndDataIgnoringNanos(nif, baseData, nextSecond)
                .orElseThrow(() -> new RuntimeException("Albarà no trobat"));
    }

    @Transactional
    public void crearAlbaraClient(AlbaraClientFormDTO dto) {
        if (dto.getNifClient() == null || dto.getNifClient().isEmpty()) throw new RuntimeException("Client obligatori.");
        if (dto.getDataProduccio() == null) throw new RuntimeException("Data obligatòria.");

        LocalDateTime baseData = dto.getDataProduccio().withNano(0);
        LocalDateTime nextSecond = baseData.plusSeconds(1);
        Optional<AlbaraClient> existing = albaraRepo.findByNifAndDataIgnoringNanos(dto.getNifClient(), baseData, nextSecond);
        
        AlbaraClient albara;
        if (existing.isPresent()) {
            albara = existing.get();
            // REGLA DE NEGOCIO: Bloquear si ya está entregado
            if (albara.getEstat() == EstatAlbaraClient.LLIURAT) {
                throw new RuntimeException("L'albarà està LLIURAT i no es pot modificar.");
            }
            albara.getLinies().clear(); // Vaciamos las líneas antiguas para poner las nuevas
        } else {
            albara = new AlbaraClient();
            Client client = clientRepo.findById(dto.getNifClient()).orElseThrow();
            albara.setClient(client);
            albara.setDataProduccio(dto.getDataProduccio());
            albara.setEstat(EstatAlbaraClient.PENDENT_DE_LLIURAR);
        }

        // Añadimos las líneas del formulario
        for (LiniaClientDTO liniaDTO : dto.getLinies()) {
            if (liniaDTO.getProducteId() != null && liniaDTO.getQuantitat() != null && liniaDTO.getQuantitat() > 0) {
                Producte producte = producteRepo.findById(liniaDTO.getProducteId()).orElseThrow();
                LiniaClient linia = new LiniaClient();
                linia.setProducte(producte);
                linia.setQuantitat(liniaDTO.getQuantitat());
                
                // AÑADIDO: Guardar el operario si lo has puesto en el formulario (opcional)
                if(liniaDTO.getOperari() != null) {
                    linia.setOperari(liniaDTO.getOperari());
                }

                albara.addLinia(linia);
            }
        }
        
        if (albara.getLinies().isEmpty()) throw new RuntimeException("Ha de contenir al menys un producte.");
        albaraRepo.save(albara);
    }

    // ARREGLADO: Ahora recibe LocalDateTime
    @Transactional
    public void eliminarAlbara(String nif, LocalDateTime data) {
        AlbaraClient albara = getAlbaraById(nif, data);
        // REGLA DE NEGOCIO: No borrar si está entregado
        if (albara.getEstat() == EstatAlbaraClient.LLIURAT) {
            throw new RuntimeException("No es pot eliminar un albarà que ja ha estat LLIURAT.");
        }
        albaraRepo.delete(albara);
    }

    @Transactional
    public void entregarAlbara(String nif, LocalDateTime data) {
        AlbaraClient albara = getAlbaraById(nif, data);
        albara.setEstat(EstatAlbaraClient.LLIURAT);
        // Marcamos también las líneas como lliuradas (como pide el enunciado)
        for(LiniaClient linia : albara.getLinies()) {
            linia.setEstatLinia(EstatLiniaClient.LLIURADA);
        }
        albaraRepo.save(albara);
    }
}