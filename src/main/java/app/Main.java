package app;

import contabancaria.ContaBancaria;
import java.util.Scanner;

public class Main {

    static ContaBancaria contaBancaria = new ContaBancaria();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int opcoes = -1;

        do {
            System.out.println("=== Seja bem-vindo ===");
            System.out.println("------------------");
            System.out.println("Escolha uma opção:");
            System.out.println("1 - Criar conta");
            System.out.println("2 - Acessar conta");
            System.out.println("3 - Verificar número de conta");
            System.out.println("4 - Verificar número de agência");
            System.out.println("0 - Sair");

            opcoes = scanner.nextInt();

            switch (opcoes) {
                case 1:
                    contaBancaria.criarConta();
                    break;

                case 2:
                    contaBancaria.acessarConta();
                    break;

                case 3:
                    System.out.println(contaBancaria.getConta());
                    break;

                case 4:
                    System.out.println(contaBancaria.getAgencia());
                    break;

                case 0:
                    System.out.println("Encerrando o programa...");
                    break;

                default:
                    System.out.println("Opção inválida");
                    break;
            }

        } while (opcoes != 0);

        scanner.close();
    }

    public static void menu() {
        int opcoes = -1;

        do {
            System.out.println("==== Menu ====");
            System.out.println("Escolha uma das opções:");
            System.out.println("1 - Consultar saldo");
            System.out.println("2 - Consultar cheque especial");
            System.out.println("3 - Depositar dinheiro");
            System.out.println("4 - Sacar dinheiro");
            System.out.println("5 - Pagar um boleto");
            System.out.println("6 - Verificar se a conta está usando cheque especial");
            System.out.println("7 - Voltar");
            System.out.println("0 - Sair");

            opcoes = scanner.nextInt();

            switch (opcoes) {
                case 1:
                    System.out.println("Saldo: R$ " + contaBancaria.getSaldo());
                    break;

                case 2:
                    System.out.println("Limite total do cheque especial: R$ "
                            + contaBancaria.getCheque_especial()
                            + "\nLimite utilizado do cheque especial: R$ "
                            + contaBancaria.getValorChequeEspecialUsado());
                    break;

                case 3:
                    contaBancaria.depositando();
                    break;

                case 4:
                    contaBancaria.sacando();
                    break;

                case 5:
                    contaBancaria.pagandoBoleto();
                    break;

                case 6:
                    if (contaBancaria.getValorChequeEspecialUsado() > 0) {
                        System.out.println("Você utilizou R$ "
                                + String.format("%.2f",
                                        contaBancaria.getValorChequeEspecialUsado())
                                + " do seu limite de cheque especial.");
                    } else {
                        System.out.println("Você não está utilizando o cheque especial.");
                    }
                    break;

                case 7:
                    return;

                case 0:
                    System.exit(0);
                    break;

                default:
                    System.out.println("Opção inválida");
                    break;
            }

        } while (true);
    }
}
