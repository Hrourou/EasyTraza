/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.validation;

/**
 *
 * @author HAMZA
 */
public class Validator {

    private static final String LETRES_NIF = "TRWAGMYFPDXBNJZSQVHLCKE";

    // =========================
    // VALIDAR NIF
    // =========================
    public static boolean validarNif(String nif) {
        if (nif == null) return false;

        nif = nif.toUpperCase().replaceAll("[\\s-]", "");

        if (nif.matches("^[XYZ]\\d{7}[A-Z]$")) {
            if (nif.startsWith("X")) nif = "0" + nif.substring(1);
            else if (nif.startsWith("Y")) nif = "1" + nif.substring(1);
            else if (nif.startsWith("Z")) nif = "2" + nif.substring(1);
        }

        if (!nif.matches("\\d{8}[A-Z]")) return false;

        int numero = Integer.parseInt(nif.substring(0, 8));
        char letra = nif.charAt(8);

        char correcta = LETRES_NIF.charAt(numero % 23);

        return letra == correcta;
    }
    /**
     * Valida el formato de un correo electrónico.
     * @param email Correo electrónico.
     * @return true si el formato es correcto.
     */
    public static boolean validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) return false;
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    /**
     * Valida que un número de teléfono español tenga 9 dígitos numéricos.
     * @param telefon Número de teléfono.
     * @return true si es válido.
     */
    public static boolean validarTelefon(String telefon) {
        if (telefon == null || telefon.trim().isEmpty()) return false;
        return telefon.matches("^[0-9]{9}$");
    }


    // =========================
    // VALIDAR CIF (REAL)
    // =========================
    public static boolean validarCif(String cif) {
        if (cif == null) return false;

        cif = cif.toUpperCase().replaceAll("[\\s-]", "");

        if (!cif.matches("[ABCDEFGHJNPQRSUVW]\\d{7}[0-9A-J]")) return false;

        char letraInicial = cif.charAt(0);
        String numeros = cif.substring(1, 8);
        char control = cif.charAt(8);

        int sumaPares = 0;
        int sumaImpares = 0;

        for (int i = 0; i < numeros.length(); i++) {
            int n = Character.getNumericValue(numeros.charAt(i));

            if ((i % 2) == 0) {
                int mult = n * 2;
                sumaImpares += (mult / 10) + (mult % 10);
            } else {
                sumaPares += n;
            }
        }

        int total = sumaPares + sumaImpares;
        int unidad = total % 10;
        int digitoControl = (unidad == 0) ? 0 : (10 - unidad);

        char letraControl = (char) ('A' + digitoControl);

        // TIPOS DE CIF
        if ("PQRSNW".indexOf(letraInicial) != -1) {
            return control == letraControl;
        } else if ("ABEH".indexOf(letraInicial) != -1) {
            return control == Character.forDigit(digitoControl, 10);
        } else {
            return control == Character.forDigit(digitoControl, 10) || control == letraControl;
        }
    }

    // =========================
    // VALIDAR GENERAL
    // =========================
    public static boolean validarNifCif(String valor) {
        return validarNif(valor) || validarCif(valor);
    }
}   