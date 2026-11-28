package transacoes;

import contabancaria.ContaBancaria;

public class Deposito extends Transacao {

    public Deposito(double valor) {
        super(valor);
    }

    @Override
    public void executar(ContaBancaria conta) {
        if (valor <= 0) {
            System.out.println("O depósito deve ser maior que zero.");
            return;
        }

        double dividaAntes = conta.getValorChequeEspecialUsado();

        if (dividaAntes > 0) {
            System.out.println("Você utilizou R$" + formatar(dividaAntes) + " do seu cheque especial.");
            System.out.println("Será cobrada uma taxa de 20%: R$" + formatar(conta.getTaxaChequeEspecialDevida()));
        }

        conta.depositar(valor);

        if (dividaAntes <= 0) {
            System.out.println("Depósito realizado com sucesso!");
        } else if (conta.getValorChequeEspecialUsado() > 0) {
            System.out.println("Depósito utilizado para abater a dívida do cheque especial.");
            System.out.println("Valor restante da dívida: R$" + formatar(conta.getValorChequeEspecialUsado()));
        } else {
            System.out.println("Cheque especial e taxa pagos com sucesso!");
        }

        exibirSaldo(conta);
    }
}
