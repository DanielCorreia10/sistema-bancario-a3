package contabancaria;

import cliente.Cliente;
import geradornumeros.GeradorNumerosFormatados;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Banco {

    private final List<Cliente> clientes = new ArrayList<>();
    private final List<ContaBancaria> contas = new ArrayList<>();

    public ContaBancaria abrirContaCorrente(Cliente titular) {
        if (!podeAbrir(titular, ContaCorrente.class)) {
            return null;
        }
        return registrar(titular, new ContaCorrente(titular,
                GeradorNumerosFormatados.gerarNumeroConta(),
                GeradorNumerosFormatados.gerarNumeroAgencia()));
    }

    public ContaBancaria abrirContaPoupanca(Cliente titular) {
        if (!podeAbrir(titular, ContaPoupanca.class)) {
            return null;
        }
        return registrar(titular, new ContaPoupanca(titular,
                GeradorNumerosFormatados.gerarNumeroConta(),
                GeradorNumerosFormatados.gerarNumeroAgencia()));
    }

    private boolean podeAbrir(Cliente titular, Class<? extends ContaBancaria> tipo) {
        Cliente cadastrado = buscarCliente(titular.getCpf());
        if (cadastrado != null && cadastrado != titular) {
            throw new IllegalArgumentException("Já existe outro cliente com este CPF.");
        }
        return !clientePossuiConta(titular, tipo);
    }

    private ContaBancaria registrar(Cliente titular, ContaBancaria conta) {
        conta.abrir();

        if (buscarCliente(titular.getCpf()) == null) {
            clientes.add(titular);
        }
        contas.add(conta);
        return conta;
    }

    public Cliente buscarCliente(String cpf) {
        for (Cliente cliente : clientes) {
            if (cliente.temCpf(cpf)) {
                return cliente;
            }
        }
        return null;
    }

    public ContaBancaria buscarConta(String numeroConta) {
        String digitos = numeroConta == null ? "" : numeroConta.replaceAll("\\D", "");

        for (ContaBancaria conta : contas) {
            if (conta.getConta().equals(digitos)) {
                return conta;
            }
        }
        return null;
    }

    public List<ContaBancaria> autenticar(String cpf, String senha) {
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

    public boolean clientePossuiConta(Cliente cliente, Class<? extends ContaBancaria> tipo) {
        for (ContaBancaria conta : contas) {
            if (conta.getTitular() == cliente && tipo.isInstance(conta)) {
                return true;
            }
        }
        return false;
    }

    public boolean contasAtivas() {
        return !contas.isEmpty();
    }

    public List<ContaBancaria> getContas() {
        return Collections.unmodifiableList(contas);
    }

    public boolean alterarNomeConta(String numeroConta, String novoNome) {
        ContaBancaria conta = buscarConta(numeroConta);
        if (conta == null) {
            return false;
        }
        conta.setNome(novoNome);
        return true;
    }

    public boolean alterarCelular(String numeroConta, String novoCelular) {
        ContaBancaria conta = buscarConta(numeroConta);
        if (conta == null) {
            return false;
        }
        conta.setCelular(novoCelular);
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
