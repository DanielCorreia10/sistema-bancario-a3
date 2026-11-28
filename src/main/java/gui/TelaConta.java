package gui;

import contabancaria.ContaBancaria;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.function.Function;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import transacoes.Deposito;
import transacoes.PagamentoBoleto;
import transacoes.Saque;
import transacoes.Transacao;

/** Operações de uma conta já autenticada: depósito, saque, boleto e rendimento. */
final class TelaConta extends JDialog {

    private final ContaBancaria conta;
    private final JLabel rotuloSaldo = new JLabel();
    private final JLabel rotuloCheque = new JLabel();
    private final JTextArea registro = new JTextArea(8, 40);

    TelaConta(Component pai, ContaBancaria conta) {
        super(SwingUtilities.getWindowAncestor(pai), conta.getTipoConta(), ModalityType.APPLICATION_MODAL);
        this.conta = conta;

        JLabel titulo = new JLabel(conta.getNome() + " - " + conta.getTipoConta());
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 16f));
        JLabel numeros = new JLabel("Conta " + Formatos.conta(conta.getConta())
                + "   |   Agência " + Formatos.agencia(conta.getAgencia()));
        rotuloSaldo.setFont(rotuloSaldo.getFont().deriveFont(Font.BOLD, 14f));

        JPanel info = new JPanel(new GridLayout(0, 1, 2, 2));
        info.setBorder(new EmptyBorder(10, 12, 6, 12));
        info.add(titulo);
        info.add(numeros);
        info.add(rotuloSaldo);
        info.add(rotuloCheque);

        registro.setEditable(false);
        registro.setLineWrap(true);
        registro.setWrapStyleWord(true);
        JScrollPane rolagem = new JScrollPane(registro);
        rolagem.setBorder(new EmptyBorder(0, 12, 0, 12));

        JButton depositar = new JButton("Depositar");
        JButton sacar = new JButton("Sacar");
        JButton boleto = new JButton("Pagar boleto");
        JButton rendimento = new JButton("Aplicar rendimento");
        JButton voltar = new JButton("Voltar");

        depositar.addActionListener(e -> executarTransacao("depósito", Deposito::new));
        sacar.addActionListener(e -> executarTransacao("saque", Saque::new));
        boleto.addActionListener(e -> executarTransacao("boleto", PagamentoBoleto::new));
        rendimento.addActionListener(e -> aplicarRendimento());
        voltar.addActionListener(e -> dispose());

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botoes.add(depositar);
        botoes.add(sacar);
        botoes.add(boleto);
        botoes.add(rendimento);
        botoes.add(voltar);

        getContentPane().add(info, BorderLayout.NORTH);
        getContentPane().add(rolagem, BorderLayout.CENTER);
        getContentPane().add(botoes, BorderLayout.SOUTH);

        atualizarInformacoes();
        registro.append("Bem-vindo(a), " + conta.getNome() + "!\n");

        pack();
        setLocationRelativeTo(pai);
    }

    private void atualizarInformacoes() {
        rotuloSaldo.setText("Saldo: " + Formatos.moeda(conta.getSaldo()));

        if (conta.getChequeEspecial() > 0) {
            rotuloCheque.setText("Cheque especial: limite " + Formatos.moeda(conta.getChequeEspecial())
                    + " | utilizado " + Formatos.moeda(conta.getValorChequeEspecialUsado()));
        } else {
            rotuloCheque.setText("Esta conta não possui cheque especial.");
        }
    }

    private void executarTransacao(String nome, Function<Double, Transacao> fabrica) {
        Double valor = pedirValor("Informe o valor do " + nome + ":", nome);
        if (valor == null) {
            return;
        }

        try {
            // Polimorfismo: a conta executa qualquer Transacao sem saber qual é a subclasse.
            registrar(conta.executarTransacao(fabrica.apply(valor)));
        } catch (IllegalArgumentException ex) {
            erro(ex.getMessage());
        }
    }

    private void aplicarRendimento() {
        double rendimento = conta.aplicarRendimento();
        if (rendimento > 0) {
            registrar("Rendimento aplicado: " + Formatos.moeda(rendimento)
                    + "\nSaldo atualizado: " + Formatos.moeda(conta.getSaldo()));
        } else {
            registrar("Este tipo de conta não possui rendimento.");
        }
    }

    private Double pedirValor(String mensagem, String titulo) {
        String texto = JOptionPane.showInputDialog(this, mensagem, titulo, JOptionPane.QUESTION_MESSAGE);
        if (texto == null) {
            return null;
        }
        try {
            return Formatos.lerValor(texto);
        } catch (NumberFormatException ex) {
            erro("Entrada inválida, digite um valor numérico.");
            return null;
        }
    }

    private void registrar(String mensagem) {
        registro.append("\n" + mensagem + "\n");
        registro.setCaretPosition(registro.getDocument().getLength());
        atualizarInformacoes();
    }

    private void erro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Atenção", JOptionPane.ERROR_MESSAGE);
    }
}
