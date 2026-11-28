package cliente;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Cliente {

    public static final int TAMANHO_MINIMO_SENHA = 6;

    private static final int ITERACOES = 65536;
    private static final int TAMANHO_HASH_BITS = 256;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final String nome;
    private final String cpf;
    private final String celular;
    private final byte[] salt;
    private final byte[] senhaHash;

    public Cliente(String nome, String cpf, String senha, String celular) {
        if (!cpfValido(cpf)) {
            throw new IllegalArgumentException("CPF inválido.");
        }
        if (!senhaValida(senha)) {
            throw new IllegalArgumentException(
                    "A senha deve ter no mínimo " + TAMANHO_MINIMO_SENHA + " caracteres.");
        }
        if (!celularValido(celular)) {
            throw new IllegalArgumentException("Celular inválido.");
        }

        this.nome = nome;
        this.cpf = cpf;
        this.celular = normalizarCelular(celular);
        this.salt = new byte[16];
        RANDOM.nextBytes(salt);
        this.senhaHash = calcularHash(senha, salt);
    }

    public static boolean cpfValido(String cpf) {
        return cpf != null && cpf.matches("\\d{11}");
    }

    public static String normalizarCelular(String entrada) {
        return entrada == null ? "" : entrada.replaceAll("\\D", "");
    }

    public static boolean celularValido(String entrada) {
        return normalizarCelular(entrada).matches("\\d{11}");
    }

    public static boolean senhaValida(String senha) {
        return senha != null && senha.length() >= TAMANHO_MINIMO_SENHA;
    }

    public boolean autenticar(String senhaInformada) {
        if (senhaInformada == null) {
            return false;
        }
        return MessageDigest.isEqual(senhaHash, calcularHash(senhaInformada, salt));
    }

    public boolean temCpf(String cpfInformado) {
        return cpf.equals(cpfInformado);
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getCelular() {
        return celular;
    }

    private static byte[] calcularHash(String senha, byte[] salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(senha.toCharArray(), salt, ITERACOES, TAMANHO_HASH_BITS);
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(spec)
                    .getEncoded();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Não foi possível processar a senha.", e);
        }
    }
}
