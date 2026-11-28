package gui;

import contabancaria.ContaBancaria;
import java.util.Locale;

/** Utilitários de formatação e leitura de valores usados pelas telas. */
final class Formatos {

    private static final Locale LOCALE_BR = new Locale("pt", "BR");

    private Formatos() {
    }

    static String moeda(double valor) {
        return "R$ " + String.format(LOCALE_BR, "%,.2f", valor);
    }

    static String percentual(double valor) {
        return String.format(LOCALE_BR, "%.2f%%", valor);
    }

    /** Aceita "10","10.5", "10,5" e "1.000,50". Lança NumberFormatException se inválido. */
    static double lerValor(String texto) {
        String t = texto.trim();
        if (t.indexOf(',') >= 0) {
            t = t.replace(".", "").replace(',', '.');
        }
        double valor = Double.parseDouble(t);
        if (Double.isNaN(valor) || Double.isInfinite(valor)) {
            throw new NumberFormatException("Valor inválido");
        }
        return valor;
    }

    static String cpf(String cpf) {
        if (cpf.length() != 11) {
            return cpf;
        }
        return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "."
                + cpf.substring(6, 9) + "-" + cpf.substring(9);
    }

    static String celular(String celular) {
        if (celular.length() != 11) {
            return celular;
        }
        return "(" + celular.substring(0, 2) + ") " + celular.substring(2, 7) + "-" + celular.substring(7);
    }

    static String conta(String conta) {
        if (conta.length() != 6) {
            return conta;
        }
        return conta.substring(0, 5) + "-" + conta.substring(5);
    }

    static String agencia(String agencia) {
        if (agencia.length() != 5) {
            return agencia;
        }
        return agencia.substring(0, 4) + "-" + agencia.substring(4);
    }

    /** Texto com todos os dados de uma conta, usado na consulta. */
    static String detalhes(ContaBancaria conta) {
        StringBuilder sb = new StringBuilder();
        sb.append("Tipo: ").append(conta.getTipoConta()).append('\n');
        sb.append("Nome da conta: ").append(conta.getNome()).append('\n');
        sb.append("Titular (cadastro): ").append(conta.getTitular().getNome()).append('\n');
        sb.append("CPF: ").append(cpf(conta.getTitular().getCpf())).append('\n');
        sb.append("Celular: ").append(celular(conta.getCelular())).append('\n');
        sb.append("Conta: ").append(conta(conta.getConta())).append('\n');
        sb.append("Agência: ").append(agencia(conta.getAgencia())).append('\n');
        sb.append("Saldo: ").append(moeda(conta.getSaldo()));
        if (conta.getChequeEspecial() > 0) {
            sb.append("\nCheque especial: limite ").append(moeda(conta.getChequeEspecial()))
                    .append(", utilizado ").append(moeda(conta.getValorChequeEspecialUsado()));
        }
        if (conta.getTaxaRendimento() > 0) {
            sb.append("\nTaxa de rendimento: ").append(percentual(conta.getTaxaRendimento()))
                    .append(" por aplicação");
        }
        return sb.toString();
    }
}
