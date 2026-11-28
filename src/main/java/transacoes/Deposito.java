package transacoes;

import contabancaria.ContaBancaria;

public class Deposito extends Transacao {

    public Deposito(double valor) {
        super(valor);
    }

    @Override
    public String executar(ContaBancaria conta) {
        if (valor <= 0) {
            return "O depósito deve ser maior que zero.";
        }

        StringBuilder msg = new StringBuilder();
        double dividaAntes = conta.getValorChequeEspecialUsado();

        if (dividaAntes > 0) {
            msg.append("Você utilizou R$").append(formatar(dividaAntes)).append(" do seu cheque especial.\n");
            msg.append("Será cobrada uma taxa de 20%: R$")
                    .append(formatar(conta.getTaxaChequeEspecialDevida())).append("\n");
        }

        conta.depositar(valor);

        if (dividaAntes <= 0) {
            msg.append("Depósito realizado com sucesso!\n");
        } else if (conta.getValorChequeEspecialUsado() > 0) {
            msg.append("Depósito utilizado para abater a dívida do cheque especial.\n");
            msg.append("Valor restante da dívida: R$")
                    .append(formatar(conta.getValorChequeEspecialUsado())).append("\n");
        } else {
            msg.append("Cheque especial e taxa pagos com sucesso!\n");
        }

        return msg.append(linhaSaldo(conta)).toString();
    }
}
