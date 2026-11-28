package transacoes;

import contabancaria.ContaBancaria;
import java.util.Locale;

public abstract class Transacao {

    protected static final Locale LOCALE_BR = new Locale("pt", "BR");

    protected final double valor;

    protected Transacao(double valor) {
        this.valor = valor;
    }

    public abstract void executar(ContaBancaria conta);

    protected static String formatar(double valor) {
        return String.format(LOCALE_BR, "%.2f", valor);
    }

    protected static void exibirSaldo(ContaBancaria conta) {
        System.out.println("Saldo atual: R$" + formatar(conta.getSaldo()));
    }
}
