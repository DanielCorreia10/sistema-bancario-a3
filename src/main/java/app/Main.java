package app;

import cliente.Cliente;
import cliente.Cpf;
import cliente.Senha;
import contabancaria.Banco;
import contabancaria.ContaBancaria;
import contabancaria.ContaCorrente;
import contabancaria.ContaPoupanca;
import geradornumeros.GeradorNumerosFormatados;
import java.io.Console;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import transacoes.Deposito;
import transacoes.PagamentoBoleto;
import transacoes.Saque;

public class Main {

    private static final Locale LOCALE_BR = new Locale("pt", "BR");
    private static final Scanner scanner = new Scanner(System.in);
    private static final Banco banco = new Banco();

    public static void main(String[] args) {
        int opcao = -1;

        do {
            System.out.println("=== Seja bem-vindo ===");
            System.out.println("Escolha uma opção:");
            System.out.println("1 - Criar conta");
            System.out.println("2 - Acessar conta");
            System.out.println("3 - Listar contas");
            System.out.println("4 - Alterar nome da conta");
            System.out.println("5 - Remover conta");
            System.out.println("0 - Sair");

            opcao = lerInt();

            switch (opcao) {
                case 1:
                    criarConta();
                    break;

                case 2:
                    ContaBancaria conta = acessarConta();
                    if (conta != null) {
                        menu(conta);
                    }
                    break;

                case 3:
                    banco.listarContas();
                    break;

                case 4:
                    alterarNomeConta();
                    break;

                case 5:
                    removerConta();
                    break;

                case 0:
                    System.out.println("Saindo do sistema...");
                    System.out.println("Até logo!");
                    break;

                default:
                    System.out.println("Opção inválida");
                    break;
            }

        } while (opcao != 0);

        scanner.close();
    }

    private static void criarConta() {
        System.out.println("Escolha o tipo de conta que deseja criar:");
        System.out.println("1 - Conta Corrente");
        System.out.println("2 - Conta Poupança");
        int tipo = lerInt();

        if (tipo != 1 && tipo != 2) {
            System.out.println("Opção inválida");
            return;
        }

        Cpf cpf = lerCpfNovo();
        if (cpf == null) {
            System.out.println("Criação de conta cancelada.");
            return;
        }

        Cliente titular = banco.buscarCliente(cpf);
        boolean clienteNovo = titular == null;

        if (clienteNovo) {
            System.out.println("Insira seu nome:");
            String nome = scanner.nextLine().trim();
            titular = new Cliente(nome, cpf, new Senha(lerSenhaNova()));
        } else {
            System.out.println("CPF já cadastrado em nome de " + titular.getNome() + ".");
            if (!titular.autenticar(lerSenha("Digite a senha para abrir uma nova conta:"))) {
                System.out.println("Senha inválida. Criação de conta cancelada.");
                return;
            }
        }

        String numeroConta = GeradorNumerosFormatados.gerarNumeroConta();
        String agencia = GeradorNumerosFormatados.gerarNumeroAgencia();

        ContaBancaria novaConta;
        if (tipo == 1) {
            novaConta = new ContaCorrente(titular, numeroConta, agencia);
        } else {
            novaConta = new ContaPoupanca(titular, numeroConta, agencia);
        }

        System.out.println("Sua conta foi criada com sucesso, veja seus dados abaixo:");
        System.out.println("Nome: " + titular.getNome() + " (" + novaConta.getTipoConta() + ")");
        System.out.println("Conta: " + numeroConta);
        System.out.println("Agência: " + agencia);
        System.out.println("=========================");

        System.out.println("Realize seu primeiro depósito para ter acesso a sua conta");
        System.out.println("Digite o valor do depósito:");
        double primeiroDeposito = lerDouble();

        if (primeiroDeposito <= 0) {
            System.out.println("O depósito deve ser maior que zero. Conta não criada.");
            return;
        }

        novaConta.abrir(primeiroDeposito);
        if (clienteNovo) {
            banco.adicionarCliente(titular);
        }
        banco.adicionarConta(novaConta);
        System.out.println("Conta aberta com sucesso! Use a opção 2 para acessá-la.");
    }

    private static ContaBancaria acessarConta() {
        if (!banco.contasAtivas()) {
            System.out.println("Nenhuma conta cadastrada.");
            return null;
        }

        System.out.println("Login");
        System.out.println("============");

        System.out.println("Informe seu CPF");
        String entradaCpf = scanner.nextLine().trim();
        String senha = lerSenha("Digite sua senha");

        List<ContaBancaria> contas;
        try {
            contas = banco.autenticar(Cpf.de(entradaCpf), senha);
        } catch (IllegalArgumentException e) {
            contas = Collections.emptyList();
        }

        if (contas.isEmpty()) {
            System.out.println("CPF ou senha inválidos. Acesso negado.");
            return null;
        }

        ContaBancaria conta = contas.size() == 1 ? contas.get(0) : escolherConta(contas);
        if (conta == null) {
            return null;
        }

        System.out.println("Login realizado com sucesso!");
        System.out.println("Bem-vindo(a) " + conta.getNome() + "!");
        return conta;
    }

    private static ContaBancaria escolherConta(List<ContaBancaria> contas) {
        System.out.println("Encontramos mais de uma conta. Escolha qual acessar:");
        for (int i = 0; i < contas.size(); i++) {
            ContaBancaria c = contas.get(i);
            System.out.println((i + 1) + " - " + c.getTipoConta() + " (" + c.getConta() + ")");
        }

        int escolha = lerInt();
        if (escolha < 1 || escolha > contas.size()) {
            System.out.println("Opção inválida");
            return null;
        }
        return contas.get(escolha - 1);
    }

    private static Cpf lerCpfNovo() {
        while (true) {
            System.out.println("Informe seu CPF (deixe em branco para cancelar):");
            String linha = scanner.nextLine().trim();

            if (linha.isEmpty()) {
                return null;
            }

            try {
                return Cpf.de(linha);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static String lerSenhaNova() {
        while (true) {
            String senha = lerSenha("Crie uma senha (mínimo " + Senha.TAMANHO_MINIMO + " caracteres):");

            if (!Senha.valida(senha)) {
                System.out.println("A senha deve ter no mínimo " + Senha.TAMANHO_MINIMO + " caracteres.");
                continue;
            }

            if (senha.equals(lerSenha("Confirme a senha:"))) {
                return senha;
            }
            System.out.println("As senhas não conferem.");
        }
    }

    private static String lerSenha(String mensagem) {
        System.out.println(mensagem);

        Console console = System.console();
        if (console != null) {
            char[] digitada = console.readPassword();
            return digitada == null ? "" : new String(digitada);
        }
        return scanner.nextLine();
    }

    private static void alterarNomeConta() {
        System.out.println("Informe o número da conta que deseja alterar:");
        String numeroConta = scanner.nextLine().trim();

        System.out.println("Informe o novo nome da conta:");
        String novoNome = scanner.nextLine().trim();

        if (banco.alterarNomeConta(numeroConta, novoNome)) {
            System.out.println("Nome da conta alterado com sucesso!");
        } else {
            System.out.println("Conta não encontrada.");
        }
    }

    private static void removerConta() {
        System.out.println("Informe o número da conta que deseja remover:");
        String numeroConta = scanner.nextLine().trim();

        System.out.println("Deseja realmente remover a conta? (S/N)");
        String confirmacao = scanner.nextLine().trim();

        if (!confirmacao.equalsIgnoreCase("S")) {
            System.out.println("Operação cancelada.");
            return;
        }

        if (banco.removerConta(numeroConta)) {
            System.out.println("Conta removida com sucesso!");
        } else {
            System.out.println("Conta não encontrada.");
        }
    }

    private static void menu(ContaBancaria conta) {
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
            System.out.println("7 - Aplicar rendimento");
            System.out.println("8 - Voltar");
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
                    aplicarRendimento(conta);
                    break;

                case 8:
                    System.out.println("Voltando ao menu principal");
                    return;

                case 0:
                    System.out.println("Saindo do sistema...");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Opção inválida");
                    break;
            }

        } while (true);
    }

    private static void aplicarRendimento(ContaBancaria conta) {
        double rendimento = conta.aplicarRendimento();

        if (rendimento > 0) {
            System.out.println("Rendimento aplicado: R$ " + formatar(rendimento));
            System.out.println("Saldo atualizado: R$ " + formatar(conta.getSaldo()));
        } else {
            System.out.println("Este tipo de conta não possui rendimento.");
        }
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
