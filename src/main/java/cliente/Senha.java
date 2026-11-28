package cliente;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class Senha {

    public static final int TAMANHO_MINIMO = 6;

    private static final int ITERACOES = 65536;
    private static final int TAMANHO_HASH_BITS = 256;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final byte[] salt;
    private final byte[] hash;

    public Senha(String texto) {
        if (!valida(texto)) {
            throw new IllegalArgumentException(
                    "A senha deve ter no mínimo " + TAMANHO_MINIMO + " caracteres.");
        }
        this.salt = new byte[16];
        RANDOM.nextBytes(salt);
        this.hash = calcularHash(texto, salt);
    }

    public static boolean valida(String texto) {
        return texto != null && texto.length() >= TAMANHO_MINIMO;
    }

    public boolean confere(String texto) {
        if (texto == null) {
            return false;
        }
        return MessageDigest.isEqual(hash, calcularHash(texto, salt));
    }

    private static byte[] calcularHash(String texto, byte[] salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(texto.toCharArray(), salt, ITERACOES, TAMANHO_HASH_BITS);
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(spec)
                    .getEncoded();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Não foi possível processar a senha.", e);
        }
    }
}
