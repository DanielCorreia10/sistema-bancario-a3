
package contabancaria;

import app.Main;
import java.util.Locale;
import java.util.Scanner;
import geradornumeros.GeradorNumerosFormatados;

public class ContaBancaria {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Locale localeBR = new Locale("pt", "BR");

    public String name;
    private String conta;
    private String agencia;
    private double saldo;
    private double cheque_especial;
    private double valorChequeEspecialUsado;
    private double deposito;
    private double saque;
    private double boleto;

    public void criarConta() {
        scanner.useLocale(localeBR);

        System.out.println("Seja bem-vindo");
        System.out.println("==================");
        System.out.println("Insira seu nome:");

        this.name = scanner.nextLine().trim();

        this.conta = GeradorNumerosFormatados.gerarNumeroConta();
        this.agencia = GeradorNumerosFormatados.gerarNumeroAgencia();

        System.out.println("Sua conta foi criada com sucesso, veja seus dados abaixo:");
        System.out.println(this.name + ":");
        System.out.println("Conta: " + this.conta);
        System.out.println("Agência: " + this.agencia);
        System.out.println("=========================");

        System.out.println("Realize seu primeiro depósito para ter acesso a sua conta");
        System.out.println("Digite o valor do depósito:");

        double primeiroDeposito = scanner.nextDouble();

        if (primeiroDeposito <= 0) {
            System.out.println("O depósito deve ser maior que zero.");
            return;
        }

        this.saldo = primeiroDeposito;

        if (primeiroDeposito <= 500) {
            this.cheque_especial = 50;
        } else {
            this.cheque_especial = primeiroDeposito / 2.0;
        }

        scanner.nextLine();
        acessarConta();
    }

    public void acessarConta() {
        boolean verificador = false;

        while (!verificador) {
            System.out.println("Login");
            System.out.println("============");

            System.out.println("Informe seu nome");
            String nameToVerify = scanner.nextLine().trim();

            System.out.println("Digite o número da sua conta");
            String contaToVerify = scanner.nextLine().trim();

            System.out.println("Digite o número da sua agência");
            String agenciaToVerify = scanner.nextLine().trim();

            if (nameToVerify.equalsIgnoreCase(this.name)
                    && contaToVerify.equals(this.conta)
                    && agenciaToVerify.equals(this.agencia)) {

                verificador = true;
                System.out.println("Login realizado com sucesso!");

            } else {
                System.out.println("Informações inválidas, tente novamente.");
            }
        }

        Main.menu();
    }

    public String getConta() {
        return conta;
    }

    public String getAgencia() {
        return agencia;
    }

    public double getSaldo() {
        return saldo;
    }

    public double getCheque_especial() {
        return cheque_especial;
    }

    public double getValorChequeEspecialUsado() {
        return valorChequeEspecialUsado;
    }

    public void depositando() {
        scanner.useLocale(localeBR);

        System.out.println("Informe o valor do depósito:");
        this.deposito = scanner.nextDouble();

        if (this.deposito <= 0) {
            System.out.println("O depósito deve ser maior que zero.");
            return;
        }

        if (this.valorChequeEspecialUsado > 0) {
            double taxaChequeEspecial = this.valorChequeEspecialUsado * 0.20;
            double valorParaCobrir = this.valorChequeEspecialUsado + taxaChequeEspecial;

            System.out.println("Você utilizou R$"
                    + String.format(localeBR, "%.2f", this.valorChequeEspecialUsado)
                    + " do seu cheque especial.");

            System.out.println("Será cobrada uma taxa de 20%: R$"
                    + String.format(localeBR, "%.2f", taxaChequeEspecial));

            if (this.deposito >= valorParaCobrir) {
                this.saldo += this.deposito - valorParaCobrir;
                this.valorChequeEspecialUsado = 0;

                System.out.println("Cheque especial e taxa pagos com sucesso!");

            } else {
                double valorRestante = valorParaCobrir - this.deposito;

                this.valorChequeEspecialUsado = valorRestante;

                System.out.println("Depósito utilizado para abater a dívida do cheque especial.");
                System.out.println("Valor restante da dívida: R$"
                        + String.format(localeBR, "%.2f", valorRestante));
            }

        } else {
            this.saldo += this.deposito;
            System.out.println("Depósito realizado com sucesso!");
        }

        System.out.println("Saldo atual: R$"
                + String.format(localeBR, "%.2f", this.saldo));
    }

    public void sacando() {
        scanner.useLocale(localeBR);

        System.out.println("Informe o valor do saque:");
        this.saque = scanner.nextDouble();

        if (this.saque <= 0) {
            System.out.println("O saque deve ser maior que zero.");
            return;
        }

        if (this.saque > this.saldo) {
            double restanteParaSacar = this.saque - this.saldo;
            double limiteDisponivel = this.cheque_especial - this.valorChequeEspecialUsado;

            if (restanteParaSacar <= limiteDisponivel) {
                this.saldo = 0;
                this.valorChequeEspecialUsado += restanteParaSacar;

                System.out.println("Saque realizado com sucesso.");
                System.out.println("Valor utilizado do cheque especial: R$"
                        + String.format(localeBR, "%.2f", this.valorChequeEspecialUsado));

            } else {
                System.out.println("Saldo + limite insuficiente, não podemos concluir a transação!");
            }

        } else {
            this.saldo -= this.saque;
            System.out.println("Saque realizado com sucesso!");
        }

        System.out.println("Saldo atual: R$"
                + String.format(localeBR, "%.2f", this.saldo));
    }

    public void pagandoBoleto() {
        scanner.useLocale(localeBR);

        System.out.println("Informe o valor do boleto:");
        this.boleto = scanner.nextDouble();

        if (this.boleto <= 0) {
            System.out.println("O valor do boleto deve ser maior que zero.");
            return;
        }

        if (this.boleto <= this.saldo) {
            this.saldo -= this.boleto;
            System.out.println("Boleto pago com sucesso!");

        } else {
            System.out.println("Saldo insuficiente, vamos tentar aplicar o limite do seu cheque especial");

            double valorParaPagarComCheque = this.boleto - this.saldo;
            double limiteDisponivel = this.cheque_especial - this.valorChequeEspecialUsado;

            if (valorParaPagarComCheque <= limiteDisponivel) {
                this.valorChequeEspecialUsado += valorParaPagarComCheque;
                this.saldo = 0;

                System.out.println("Transação concluída com sucesso.");
                System.out.println("Sua conta agora está utilizando o cheque especial.");

            } else {
                System.out.println("Saldo + limite insuficiente, não podemos concluir a transação!");
            }
        }

        System.out.println("Saldo atual: R$"
                + String.format(localeBR, "%.2f", this.saldo));
    }
}
