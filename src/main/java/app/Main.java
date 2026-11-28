package app;

import contabancaria.ContaBancaria;
import contabancaria.ContaCorrente;
import contabancaria.ContaPoupanca;
import geradornumeros.GeradorNumerosFormatados;
import java.util.Locale;
import java.util.Scanner;
import transacoes.Deposito;
import transacoes.PagamentoBoleto;
import transacoes.Saque;

public class Main {

    private static final Locale LOCALE_BR = new Locale("pt", "BR");
    private static final Scanner scanner = new Scanner(System.in);

    private static ContaBancaria conta;

    public static void main(String[] args) {
        int opcao = -1;

        do {
            System.out.println("=== Seja bem-vindo ===");
            System.out.println("------------------");
            System.out.println("Escolha uma opção:");
            System.out.println("1 - Criar conta");
            System.out.println("2 - Acessar conta");
            System.out.println("3 - Verificar número de conta");
            System.out.println("4 - Verificar número de agência");
            System.out.println("0 - Sair");

            opcao = lerInt();

            switch (opcao) {
                case 1:
                    criarConta();
                    break;

                case 2:
                    if (verificarContaExiste()) {
                        acessarConta();
                    }
                    break;

                case 3:
                    if (verificarContaExiste()) {
                        System.out.println(conta.getConta());
                    }
                    break;

                case 4:
                    if (verificarContaExiste()) {
                        System.out.println(conta.getAgencia());
                    }
                    break;

                case 0:
                    System.out.println("Encerrando o programa...");
                    break;

                default:
                    System.out.println("Opção inválida");
                    break;
            }

        } while (opcao != 0);

        scanner.close();
    }

    private static void criarConta() {
        System.out.println("Seja bem-vindo");
        System.out.println("==================");
        System.out.println("Insira seu nome:");
        String nome = scanner.nextLine().trim();

        System.out.println("Escolha o tipo de conta:");
        System.out.println("1 - Conta Corrente");
        System.out.println("2 - Conta Poupança");
        int tipo = lerInt();

        String numeroConta = GeradorNumerosFormatados.gerarNumeroConta();
        String agencia = GeradorNumerosFormatados.gerarNumeroAgencia();

        ContaBancaria novaConta;
        switch (tipo) {
            case 1:
                novaConta = new ContaCorrente(nome, numeroConta, agencia);
                break;
            case 2:
                novaConta = new ContaPoupanca(nome, numeroConta, agencia);
                break;
            default:
                System.out.println("Tipo de conta inválido.");
                return;
        }

        System.out.println("Sua conta foi criada com sucesso, veja seus dados abaixo:");
        System.out.println(nome + " (" + novaConta.getTipoConta() + "):");
        System.out.println("Conta: " + numeroConta);
        System.out.println("Agência: " + agencia);
        System.out.println("=========================");

        System.out.println("Realize seu primeiro depósito para ter acesso a sua conta");
        System.out.println("Digite o valor do depósito:");
        double primeiroDeposito = lerDouble();

        if (primeiroDeposito <= 0) {
            System.out.println("O depósito deve ser maior que zero.");
            return;
        }

        novaConta.abrir(primeiroDeposito);
        conta = novaConta;

        acessarConta();
    }

    private static void acessarConta() {
        boolean autenticado = false;

        while (!autenticado) {
            System.out.println("Login");
            System.out.println("============");

            System.out.println("Informe seu nome");
            String nome = scanner.nextLine().trim();

            System.out.println("Digite o número da sua conta");
            String numeroConta = scanner.nextLine().trim();

            System.out.println("Digite o número da sua agência");
            String agencia = scanner.nextLine().trim();

            if (conta.autenticar(nome, numeroConta, agencia)) {
                autenticado = true;
                System.out.println("Login realizado com sucesso!");
            } else {
                System.out.println("Informações inválidas, tente novamente.");
            }
        }

        menu();
    }

    private static void menu() {
        int opcao = -1;

        do {
            System.out.println("==== Menu (" + conta.getTipoConta() + ") ====");
            System.out.println("Escolha uma das opções:");
            System.out.println("1 - Consultar saldo");
            System.out.println("2 - Consultar cheque especial");
            System.out.println("3 - Depositar dinheiro");
            System.out.println("4 - Sacar dinheiro");
            System.out.println("5 - Pagar um boleto");
            System.out.println("6 - Verificar se a conta está usando cheque especial");
            System.out.println("7 - Voltar");
            System.out.println("0 - Sair");

            opcao = lerInt();

            switch (opcao) {
                case 1:
                    System.out.println("Saldo: R$ " + formatar(conta.getSaldo()));
                    break;

                case 2:
                    System.out.println("Limite total do cheque especial: R$ "
                            + formatar(conta.getChequeEspecial())
                            + "\nLimite utilizado do cheque especial: R$ "
                            + formatar(conta.getValorChequeEspecialUsado()));
                    break;

                case 3:
                    System.out.println("Informe o valor do depósito:");
                    conta.executarTransacao(new Deposito(lerDouble()));
                    break;

                case 4:
                    System.out.println("Informe o valor do saque:");
                    conta.executarTransacao(new Saque(lerDouble()));
                    break;

                case 5:
                    System.out.println("Informe o valor do boleto:");
                    conta.executarTransacao(new PagamentoBoleto(lerDouble()));
                    break;

                case 6:
                    if (conta.getValorChequeEspecialUsado() > 0) {
                        System.out.println("Você utilizou R$ "
                                + formatar(conta.getValorChequeEspecialUsado())
                                + " do seu limite de cheque especial.");
                    } else {
                        System.out.println("Você não está utilizando o cheque especial.");
                    }
                    break;

                case 7:
                    return;

                case 0:
                    System.out.println("Encerrando o programa...");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Opção inválida");
                    break;
            }

        } while (true);
    }

    private static boolean verificarContaExiste() {
        if (conta == null) {
            System.out.println("Nenhuma conta criada ainda. Crie uma conta primeiro.");
            return false;
        }
        return true;
    }

    private static String formatar(double valor) {
        return String.format(LOCALE_BR, "%.2f", valor);
    }

    private static int lerInt() {
        while (true) {
            String linha = scanner.nextLine().trim();
            try {
                return Integer.parseInt(linha);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida, digite um número inteiro:");
            }
        }
    }

    private static double lerDouble() {
        while (true) {
            String linha = scanner.nextLine().trim().replace(',', '.');
            try {
                return Double.parseDouble(linha);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida, digite um valor numérico:");
            }
        }
    }
}
