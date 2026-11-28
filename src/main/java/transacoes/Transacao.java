package transacoes;

import contabancaria.ContaBancaria;
import java.util.Locale;

public abstract class Transacao {

    protected static final Locale LOCALE_BR = new Locale("pt", "BR");

    protected final double valor;

    protected Transacao(double valor) {
        this.valor = valor;
    }

    /** Executa a transação na conta e devolve a mensagem de resultado para ser exibida ao usuário. */
    public abstract String executar(ContaBancaria conta);

    protected static String formatar(double valor) {
        return String.format(LOCALE_BR, "%.2f", valor);
    }

    protected static String linhaSaldo(ContaBancaria conta) {
        return "Saldo atual: R$" + formatar(conta.getSaldo());
    }
}
