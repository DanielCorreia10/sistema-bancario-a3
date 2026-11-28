package contabancaria;

import cliente.Cliente;
import cliente.Cpf;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Banco {

    private final List<Cliente> clientes = new ArrayList<>();
    private final List<ContaBancaria> contas = new ArrayList<>();

    public void adicionarCliente(Cliente cliente) {
        clientes.add(cliente);
    }

    public Cliente buscarCliente(Cpf cpf) {
        for (Cliente cliente : clientes) {
            if (cliente.getCpf().equals(cpf)) {
                return cliente;
            }
        }
        return null;
    }

    public void adicionarConta(ContaBancaria conta) {
        contas.add(conta);
    }

    public ContaBancaria buscarConta(String numeroConta) {
        for (ContaBancaria conta : contas) {
            if (conta.getConta().equalsIgnoreCase(numeroConta)) {
                return conta;
            }
        }
        return null;
    }

    public List<ContaBancaria> autenticar(Cpf cpf, String senha) {
        List<ContaBancaria> autenticadas = new ArrayList<>();

        Cliente cliente = buscarCliente(cpf);
        if (cliente == null || !cliente.autenticar(senha)) {
            return autenticadas;
        }

        for (ContaBancaria conta : contas) {
            if (conta.getTitular() == cliente) {
                autenticadas.add(conta);
            }
        }
        return autenticadas;
    }

    public boolean contasAtivas() {
        return !contas.isEmpty();
    }

    public void listarContas() {
        if (contas.isEmpty()) {
            System.out.println("Nenhuma conta cadastrada.");
            return;
        }

        System.out.println("Contas cadastradas:");
        for (ContaBancaria conta : contas) {
            System.out.println("Nome: " + conta.getNome()
                    + ", Tipo: " + conta.getTipoConta()
                    + ", Conta: " + conta.getConta()
                    + ", Agência: " + conta.getAgencia()
                    + ", Saldo: " + String.format(new Locale("pt", "BR"), "%.2f", conta.getSaldo()));
        }
    }

    public boolean alterarNomeConta(String numeroConta, String novoNome) {
        ContaBancaria conta = buscarConta(numeroConta);
        if (conta == null) {
            return false;
        }
        conta.getTitular().setNome(novoNome);
        return true;
    }

    public boolean removerConta(String numeroConta) {
        ContaBancaria conta = buscarConta(numeroConta);
        if (conta == null) {
            return false;
        }
        return contas.remove(conta);
    }
}
