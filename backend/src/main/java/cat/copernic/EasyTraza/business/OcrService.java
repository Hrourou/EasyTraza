/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.business;

import cat.copernic.EasyTraza.dto.AlbaraLotFormDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

/**
 *
 * @author HAMZA
 */
@Service
public class OcrService {

    // Spring lee la clave de tu application.properties automáticamente
    @Value("${gemini.api.key}")
    private String apiKey;

    public AlbaraLotFormDTO llegirAlbara(MultipartFile file) {
        try {
            // 1. Convertir la foto a Base64 (El formato que entiende Internet)
            String base64Image = Base64.getEncoder().encodeToString(file.getBytes());
            String mimeType = file.getContentType();
            if (mimeType == null) mimeType = "image/jpeg";

            // 2. El "Prompt" (Las instrucciones para la Inteligencia Artificial)
            String prompt = "Ets un sistema OCR especialitzat en albarans de proveïdor d'una pastisseria.\n\n" +

            "REGLA CRÍTICA - CIF DEL PROVEÏDOR:\n" +
            "- El receptor SEMPRE és Pastisseria Saint Honoré amb NIF J59087312 o ESJ59087312.\n" +
            "- El camp cifProveidor ha de contenir el CIF/NIF de l'empresa que ENVIA la mercaderia.\n" +
            "- MAI retornis J59087312 ni ESJ59087312 com a cifProveidor. Si només trobes aquest NIF, " +
            "busca un altre identificador fiscal al document (capçalera, peu de pàgina, bloc empresa emisora).\n\n" +

            "REGLA PER AL NÚMERO DE LOT (idLot):\n" +
            "- Busca el número de lot dins de la descripció de cada producte en aquests formats:\n" +
            "  · Lletra L seguida de dígits: L602248, L602265\n" +
            "  · Paraula LOT seguida de dígits: LOT 253140301\n" +
            "  · 4 dígits que representin mes i any: 0926, 0725\n" +
            "  · Codis alfanumèrics entre parèntesis dins la descripció: (416136), (412193)\n" +
            "  · Qualsevol seqüència que comenci per M seguida de 7 dígits: M1958261\n" +
            "- Si el producte NO té número de lot visible al document, retorna idLot com a cadena buida \"\".\n" +
            "- MAI retornis null, sempre retorna string (amb valor o buit).\n\n" +

            "REGLA PER AL NOM DE LA MATÈRIA PRIMERA (nomMateriaPrimeraDetectada):\n" +
            "- Elimina del nom: pesos en kg, g, lb i els seus números (ex: '10 kg', '20 kg', '15 kg').\n" +
            "- Elimina del nom: codis de lot entre parèntesis (ex: '(412193)', '(416136)').\n" +
            "- Elimina del nom: codis d'article numèrics al principi (ex: '427214', '0301008').\n" +
            "- CONSERVA al nom: volums en cc, ml, cl, l perquè diferencien el producte " +
            "(ex: '500 cc', '1000 cc', '500 ml'). Exemple correcte: 'Tarrina Nata 500cc Blanca'.\n" +
            "- CONSERVA al nom: colors, adjectius i descriptors que diferencien variants del mateix producte " +
            "(ex: 'Blanca', 'Negra', 'Integral', 'Sense Gluten').\n" +
            "- Exemples de neteja correcta:\n" +
            "  · 'Saint Auvent croissant 10 kg(412193)' → 'Saint Auvent Croissant'\n" +
            "  · 'AMBAR B-90 Hojaldre 20 kg' → 'AMBAR B-90 Hojaldre'\n" +
            "  · 'Llard dur 15 kg - LABORA' → 'Llard Dur LABORA'\n" +
            "  · 'Tarrina Nata 500 cc. Blanca (50 u.)' → 'Tarrina Nata 500cc Blanca'\n" +
            "  · 'Tarrina Nata 1000 cc. Blanca (25 u.)' → 'Tarrina Nata 1000cc Blanca'\n" +
            "  · 'CH-B FIDEO CHKD (1kg)LOT 253140301' → 'CH-B Fideo CHKD'\n\n" +

            "Extreu les dades en aquest format JSON exacte:\n" +
            "{\n" +
            "  \"cifProveidor\": \"CIF o NIF de l'empresa emissora (mai J59087312)\",\n" +
            "  \"nomProveidor\": \"Nom de l'empresa emissora\",\n" +
            "  \"dataRecepcio\": \"YYYY-MM-DD\",\n" +
            "  \"liniesLote\": [\n" +
            "    {\n" +
            "      \"nomMateriaPrimeraDetectada\": \"nom net sense kg ni codis de lot\",\n" +
            "      \"idLot\": \"número de lot o cadena buida si no n'hi ha\",\n" +
            "      \"unitats\": 0\n" +
            "    }\n" +
            "  ]\n" +
            "}\n\n" +
            "Retorna ÚNICAMENT el JSON sense text addicional ni marques de codi.";

            // 3. Preparar el paquete de datos para enviar a Google
            Map<String, Object> inlineData = new HashMap<>();
            inlineData.put("mime_type", mimeType);
            inlineData.put("data", base64Image);

            Map<String, Object> partImage = new HashMap<>();
            partImage.put("inline_data", inlineData);

            Map<String, Object> partText = new HashMap<>();
            partText.put("text", prompt);

            Map<String, Object> content = new HashMap<>();
            content.put("parts", List.of(partText, partImage));

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("contents", List.of(content));

            // 4. Hacer la llamada a Internet (API de Gemini 1.5 Flash)
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + apiKey;
            String responseBody = restTemplate.postForObject(url, request, String.class);

            // 5. Desempaquetar la respuesta de Gemini
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(responseBody);
            String extractedJsonText = rootNode
                    .path("candidates").get(0)
                    .path("content")
                    .path("parts").get(0)
                    .path("text").asText();

            // Limpieza extra por si Gemini añade ```json al principio
            extractedJsonText = extractedJsonText.replace("```json", "").replace("```", "").trim();

            System.out.println("\n--- RESPUESTA DE GEMINI IA ---");
            System.out.println(extractedJsonText);
            System.out.println("---------------------------------------\n");

            // 6. ¡Magia Final! Convertimos el texto de Gemini directamente a tu objeto Java DTO
            return mapper.readValue(extractedJsonText, AlbaraLotFormDTO.class);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al procesar la imagen con Gemini IA: " + e.getMessage());
        }
    }
}