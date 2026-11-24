package contabancaria;

import java.util.ArrayList;

public class Banco {

    private ArrayList<ContaBancaria> contas = new ArrayList<>();

    public void adicionarConta(ContaBancaria conta) {
        contas.add(conta);
    }

    public ContaBancaria buscarConta(String gerarNumeroConta) {
        for (ContaBancaria conta : contas) {
            if (conta.getConta().equalsIgnoreCase(gerarNumeroConta)) {
                return conta;
            }
        }
        System.out.println("Conta não encontrada.");
        return null;
    }

    public boolean contasAtivas() {
        return !contas.isEmpty();
    }


    public void listarContas(){
        if (contas.isEmpty()){
            System.out.println("Nenhuma conta cadastrada.");
        } else {
            System.out.println("Contas cadastradas:");
            for (ContaBancaria listaConta : contas) {
                System.out.println("Nome: " + listaConta.getName() + ", Conta: " + listaConta.getConta() + ", Agência: " + listaConta.getAgencia() + ", Saldo: " + String.format("%.2f", listaConta.getSaldo()));
            }
        }
    }

    public void alterarNomeConta(String gerarNumeroConta, String novoNome) {
        ContaBancaria conta = buscarConta(gerarNumeroConta);
        if (conta != null) {
            conta.setName(novoNome);
            System.out.println("Nome da conta alterado com sucesso!");
        }
    }

    public void removerConta(String gerarNumeroConta) {
        ContaBancaria conta = buscarConta(gerarNumeroConta);
        if (conta != null) {
            contas.remove(conta);
            System.out.println("Conta removida com sucesso!");
        }
    }

}

