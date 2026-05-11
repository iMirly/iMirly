package tfg.imirly.messages.application.service;

import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class ProfanityFilterService {

    // Lista ampliada de insultos y palabras prohibidas en español
    private final List<String> badWords = Arrays.asList(
            // Muy fuertes / Ofensivos
            "mierda", "cabron", "cabrón", "cabrona", "puta", "puto", "puton", "putón", "mamon", "mamón", "mamona",
            "gilipollas", "joder", "zorra", "zorro", "maricon", "maricón", "marica", "pendejo", "pendeja",
            "bastardo", "bastarda", "hijo de puta", "hdp", "malnacido", "malnacida",
            
            // Insultos comunes y descalificaciones
            "subnormal", "desgraciado", "desgraciada", "imbecil", "imbécil", "idiota", 
            "estupido", "estúpida", "estupida", "estúpido", "asqueroso", "asquerosa",
            "cornudo", "payaso", "payasa", "engendro", "basura", "rata", "cerdo", "cerda",
            "tonto", "tonta", "retrasado", "retrasada", "mongolo", "mongola", "maldito", "maldita"
    );

    /**
     * Reemplaza las palabras prohibidas por asteriscos en el texto.
     */
    public String filterText(String input) {
        if (input == null || input.trim().isEmpty()) {
            return input;
        }

        String filteredText = input;
        for (String word : badWords) {
            // Usamos regex con límite de palabra (\b) para mayor precisión.
            // Pattern.quote por si alguna palabra tuviera caracteres especiales de regex.
            String regex = "(?i)\\b" + Pattern.quote(word) + "\\b";
            String replacement = "*".repeat(word.length());
            filteredText = filteredText.replaceAll(regex, replacement);
        }
        return filteredText;
    }

    /**
     * Verifica si el texto contiene alguna palabra prohibida.
     */
    public boolean containsProfanity(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }
        
        String lowerInput = input.toLowerCase();
        
        for (String word : badWords) {
            // Buscamos la palabra exacta usando límites de palabra para evitar falsos positivos
            // (ej: no detectar "imbécil" dentro de otra palabra inexistente que la contenga)
            String regex = "(?i).*\\b" + Pattern.quote(word) + "\\b.*";
            if (lowerInput.matches(regex)) {
                return true;
            }
        }
        return false;
    }
}
