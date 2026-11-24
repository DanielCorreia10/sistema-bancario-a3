package app;

import contabancaria.ContaBancaria;
import contabancaria.ContaCorrente;
import contabancaria.ContaPoupanca;
import contabancaria.Banco;
import java.util.Scanner;

public class Main {

    static final Banco banco = new Banco();
    static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        var opcoes = -1;
        do {

            System.out.println("===Seja bem-vindo===");
            System.out.println("Escolha uma opção:");
            System.out.println("1-Criar conta");
            System.out.println("2-Acessar conta");
            System.out.println("3-Listar contas");
            System.out.println("4-Alterar nome da conta");
            System.out.println("5-Remover conta");
            System.out.println("0-Sair");
            opcoes = scanner.nextInt();
            scanner.nextLine();

            switch (opcoes) {
                case 1 -> tipoConta();
                case 2 -> {
                    ContaBancaria conta = acessarConta();

                    if (conta != null) {
                        menu(conta);
                    }
                }
                case 3 -> banco.listarContas();
                case 4 -> {
                    System.out.println("Informe o número da conta que deseja alterar:");
                    String numero = scanner.nextLine().trim();
                    System.out.println("Informe o novo nome da conta:");
                    String novoNome = scanner.nextLine().trim();
                    banco.alterarNomeConta(numero, novoNome);
                }
                case 5 -> {
                    System.out.println("Informe o número da conta que deseja remover:");
                    String numero = scanner.nextLine().trim();
                    System.out.println("Deseja realmente remover a conta? (S/N)");
                    String confirmacao = scanner.nextLine().trim();
                    if (confirmacao.equalsIgnoreCase("S")) {
                        banco.removerConta(numero);
                    } else {
                        System.out.println("Operação cancelada.");
                    }
                }
                case 0 -> System.out.println("Saindo do sistema...\n Até logo!");

            }

        } while (true);

    }

    public static void tipoConta() {
        System.out.println("Escolha o tipo de conta que deseja criar:");
        System.out.println("1-Conta Corrente");
        System.out.println("2-Conta Poupança");
        int tipo = scanner.nextInt();
        scanner.nextLine();

        switch (tipo) {
            case 1 -> {
                ContaCorrente corrente = new ContaCorrente();
                corrente.criarConta();
                banco.adicionarConta(corrente);
            }
            case 2 -> {
                ContaPoupanca poupanca = new ContaPoupanca();
                poupanca.criarConta();
                banco.adicionarConta(poupanca);
            }
            default -> System.out.println("Opção inválida");
        }
    }

    public static ContaBancaria acessarConta() {
        if (!banco.contasAtivas()) {
            System.out.println("Nenhuma conta cadastrada.");
            return null;
        }

        System.out.println("Login");
        System.out.println("============");
        System.out.println("Informe seu nome");
        String nome = scanner.nextLine().trim();
        System.out.println("Digite o número da sua conta");
        String numero = scanner.nextLine().trim();
        System.out.println("Digite o número da sua agência");
        String agencia = scanner.nextLine().trim();

        ContaBancaria conta = banco.buscarConta(numero);
        if (conta == null) {
            System.out.println("Informações inválidas");
            return null;
        }
        if (conta.autorizarAcesso(nome, numero, agencia)) {
            System.out.println("Bem-vindo(a) " + conta.getName() + "!");
            return conta;
        }
        return null;
    }

    public static void menu(ContaBancaria conta) {
        var opcoes = -1;
        do {
            System.out.println("====Menu====");
            System.out.println("Escolha uma das opções");
            System.out.println("1-Consultar saldo");
            System.out.println("2-Consultar cheque especial");
            System.out.println("3-Depositar dinheiro");
            System.out.println("4-Sacar dinheiro");
            System.out.println("5-Pagar um boleto");
            System.out.println("6-Verificar se a conta está usando cheque especial");
            System.out.println("7-Voltar");
            System.out.println("0-Sair");
            opcoes = scanner.nextInt();

            switch (opcoes) {
                case 1 -> System.out.println("Saldo: R$" + conta.getSaldo());
                case 2 -> {
                    if (conta instanceof ContaCorrente corrente) {
                        System.out.println("Limite total do cheque especial: R$" + corrente.getCheque_especial() +
                                "\n Limite utilizado do cheque especial: R$" + corrente.getValorChequeEspecialUsado());
                    }
                }
                case 3 -> conta.depositando();
                case 4 -> conta.sacando();
                case 5 -> conta.pagandoBoleto();
                case 6 -> {
                    if (conta instanceof ContaCorrente corrente) {
                        System.out.println(
                                "Você utilizou R$" + String.format("%.2f", corrente.getValorChequeEspecialUsado())
                                        + " do seu limite de cheque especial.");
                    } else {
                        System.out.println("Você não está utilizando o cheque especial.");
                    }
                }
                case 7 -> {
                    System.out.println("Voltando ao menu principal");
                    return;
                }
                case 0 -> System.exit(0);
                default -> System.out.println("Opção inválida");
            }
        } while (true);

    }

}