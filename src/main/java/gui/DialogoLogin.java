package gui;

import contabancaria.Banco;
import contabancaria.ContaBancaria;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/** Pede CPF e senha e devolve a conta escolhida (ou null se cancelado/inválido). */
final class DialogoLogin {

    private DialogoLogin() {
    }

    static ContaBancaria mostrar(Component pai, Banco banco) {
        if (!banco.contasAtivas()) {
            JOptionPane.showMessageDialog(pai, "Nenhuma conta cadastrada.", "Acessar conta",
                    JOptionPane.INFORMATION_MESSAGE);
            return null;
        }

        JTextField campoCpf = new JTextField(14);
        JPasswordField campoSenha = new JPasswordField(14);

        JPanel painel = new JPanel(new GridLayout(0, 1, 4, 4));
        painel.add(new JLabel("CPF (somente números):"));
        painel.add(campoCpf);
        painel.add(new JLabel("Senha:"));
        painel.add(campoSenha);

        int opcao = JOptionPane.showConfirmDialog(pai, painel, "Acessar conta",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opcao != JOptionPane.OK_OPTION) {
            return null;
        }

        String cpf = campoCpf.getText().replaceAll("\\D", "");
        List<ContaBancaria> contas = banco.autenticar(cpf, new String(campoSenha.getPassword()));

        if (contas.isEmpty()) {
            JOptionPane.showMessageDialog(pai, "CPF ou senha inválidos. Acesso negado.", "Acessar conta",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
        if (contas.size() == 1) {
            return contas.get(0);
        }
        return escolherConta(pai, contas);
    }

    private static ContaBancaria escolherConta(Component pai, List<ContaBancaria> contas) {
        String[] opcoes = new String[contas.size()];
        for (int i = 0; i < contas.size(); i++) {
            ContaBancaria c = contas.get(i);
            opcoes[i] = c.getTipoConta() + " (" + Formatos.conta(c.getConta()) + ")";
        }

        Object escolhida = JOptionPane.showInputDialog(pai,
                "Encontramos mais de uma conta. Escolha qual acessar:", "Acessar conta",
                JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
        if (escolhida == null) {
            return null;
        }
        for (int i = 0; i < opcoes.length; i++) {
            if (opcoes[i].equals(escolhida)) {
                return contas.get(i);
            }
        }
        return null;
    }
}
