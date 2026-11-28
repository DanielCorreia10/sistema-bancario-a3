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

    private String nome;
    private final String cpf;
    private final byte[] salt;
    private final byte[] senhaHash;

    public Cliente(String nome, String cpf, String senha) {
        if (!cpfValido(cpf)) {
            throw new IllegalArgumentException("CPF inválido.");
        }
        if (!senhaValida(senha)) {
            throw new IllegalArgumentException(
                    "A senha deve ter no mínimo " + TAMANHO_MINIMO_SENHA + " caracteres.");
        }

        this.nome = nome;
        this.cpf = cpf;
        this.salt = new byte[16];
        RANDOM.nextBytes(salt);
        this.senhaHash = calcularHash(senha, salt);
    }

    public static boolean cpfValido(String cpf) {
        return cpf != null && cpf.matches("\\d{11}");
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

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
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
