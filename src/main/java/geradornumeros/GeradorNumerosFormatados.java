package geradornumeros;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GeradorNumerosFormatados {
    private static final Random random = new Random();
    private static final Set<String> contasGeradas = new HashSet<>();
    private static final Set<String> agenciasGeradas = new HashSet<>();

    public static String gerarNumeroConta() {
        String numero;
        do {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 5; i++) {
                sb.append(random.nextInt(10));
            }
            sb.append('-').append(random.nextInt(10));
            numero = sb.toString();
        } while (contasGeradas.contains(numero));

        contasGeradas.add(numero);
        return numero;
    }

    public static String gerarNumeroAgencia() {
        String numero;
        do {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 4; i++) {
                sb.append(random.nextInt(10));
            }
            sb.append('-').append(random.nextInt(10));
            numero = sb.toString();
        } while (agenciasGeradas.contains(numero));

        agenciasGeradas.add(numero);
        return numero;
    }
}
