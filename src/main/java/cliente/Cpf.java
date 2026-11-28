package cliente;

public final class Cpf {

    private final String numero;

    private Cpf(String numero) {
        this.numero = numero;
    }

    public static Cpf de(String entrada) {
        String digitos = entrada == null ? "" : entrada.replaceAll("\\D", "");

        if (!valido(digitos)) {
            throw new IllegalArgumentException("CPF inválido.");
        }
        return new Cpf(digitos);
    }

    public static boolean valido(String digitos) {
        if (digitos == null || !digitos.matches("\\d{11}")) {
            return false;
        }
        if (digitos.matches("(\\d)\\1{10}")) {
            return false;
        }
        return calcularDigito(digitos, 9) == digitos.charAt(9) - '0'
                && calcularDigito(digitos, 10) == digitos.charAt(10) - '0';
    }

    private static int calcularDigito(String digitos, int tamanho) {
        int soma = 0;
        for (int i = 0; i < tamanho; i++) {
            soma += (digitos.charAt(i) - '0') * (tamanho + 1 - i);
        }
        int resto = (soma * 10) % 11;
        return resto == 10 ? 0 : resto;
    }

    @Override
    public boolean equals(Object outro) {
        if (this == outro) {
            return true;
        }
        if (!(outro instanceof Cpf)) {
            return false;
        }
        return numero.equals(((Cpf) outro).numero);
    }

    @Override
    public int hashCode() {
        return numero.hashCode();
    }

    @Override
    public String toString() {
        return numero.substring(0, 3) + "." + numero.substring(3, 6) + "."
                + numero.substring(6, 9) + "-" + numero.substring(9);
    }
}
