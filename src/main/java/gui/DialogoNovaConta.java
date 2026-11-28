package gui;

import cliente.Cliente;
import contabancaria.Banco;
import contabancaria.ContaBancaria;
import contabancaria.ContaCorrente;
import contabancaria.ContaPoupanca;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/** Formulário de criação de uma nova conta (uma por vez). */
final class DialogoNovaConta extends JDialog {

    private static final String CORRENTE = "Conta Corrente";
    private static final String POUPANCA = "Conta Poupança";

    private final Banco banco;
    private final JComboBox<String> comboTipo = new JComboBox<>(new String[] {CORRENTE, POUPANCA});
    private final JTextField campoCpf = new JTextField(16);
    private final JTextField campoNome = new JTextField(16);
    private final JTextField campoCelular = new JTextField(16);
    private final JPasswordField campoSenha = new JPasswordField(16);
    private final JPasswordField campoConfirmacao = new JPasswordField(16);
    private final JLabel aviso = new JLabel(" ");

    private ContaBancaria resultado;

    private DialogoNovaConta(Component pai, Banco banco) {
        super(SwingUtilities.getWindowAncestor(pai), "Criar nova conta", ModalityType.APPLICATION_MODAL);
        this.banco = banco;

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(12, 12, 4, 12));
        adicionar(form, 0, "Tipo de conta:", comboTipo);
        adicionar(form, 1, "CPF (11 números):", campoCpf);
        adicionar(form, 2, "Nome:", campoNome);
        adicionar(form, 3, "Celular com DDD:", campoCelular);
        adicionar(form, 4, "Senha (mín. " + Cliente.TAMANHO_MINIMO_SENHA + "):", campoSenha);
        adicionar(form, 5, "Confirmar senha:", campoConfirmacao);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 6;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(6, 0, 0, 0);
        form.add(aviso, c);

        JButton criar = new JButton("Criar conta");
        JButton cancelar = new JButton("Cancelar");
        criar.addActionListener(e -> criarConta());
        cancelar.addActionListener(e -> dispose());

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botoes.add(cancelar);
        botoes.add(criar);

        getContentPane().add(form, BorderLayout.CENTER);
        getContentPane().add(botoes, BorderLayout.SOUTH);
        getRootPane().setDefaultButton(criar);

        campoCpf.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                atualizarModo();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                atualizarModo();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                atualizarModo();
            }
        });

        pack();
        setResizable(false);
        setLocationRelativeTo(pai);
    }

    /** Abre o formulário e devolve a conta criada, ou null se cancelado. */
    static ContaBancaria mostrar(Component pai, Banco banco) {
        DialogoNovaConta dialogo = new DialogoNovaConta(pai, banco);
        dialogo.setVisible(true);
        return dialogo.resultado;
    }

    private static void adicionar(JPanel painel, int linha, String rotulo, Component campo) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = linha;
        c.insets = new Insets(3, 3, 3, 3);
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0;
        painel.add(new JLabel(rotulo), c);

        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        painel.add(campo, c);
    }

    private String cpfDigitado() {
        return campoCpf.getText().replaceAll("\\D", "");
    }

    /** Se o CPF já é cliente, só a senha existente é necessária. */
    private void atualizarModo() {
        boolean clienteExistente = banco.buscarCliente(cpfDigitado()) != null;
        campoNome.setEnabled(!clienteExistente);
        campoCelular.setEnabled(!clienteExistente);
        campoConfirmacao.setEnabled(!clienteExistente);
        aviso.setText(clienteExistente ? "Você já é nosso cliente: informe sua senha atual." : " ");
        pack();
    }

    private void criarConta() {
        try {
            String cpf = cpfDigitado();
            if (!Cliente.cpfValido(cpf)) {
                throw new IllegalArgumentException("CPF inválido. Digite os 11 números.");
            }

            boolean corrente = CORRENTE.equals(comboTipo.getSelectedItem());
            Class<? extends ContaBancaria> tipo = corrente ? ContaCorrente.class : ContaPoupanca.class;
            String senha = new String(campoSenha.getPassword());

            Cliente titular = banco.buscarCliente(cpf);
            if (titular == null) {
                String nome = campoNome.getText().trim();
                if (nome.isEmpty()) {
                    throw new IllegalArgumentException("Informe o nome.");
                }
                if (!Cliente.celularValido(campoCelular.getText())) {
                    throw new IllegalArgumentException("Celular inválido. Informe DDD + número (11 dígitos).");
                }
                if (!Cliente.senhaValida(senha)) {
                    throw new IllegalArgumentException(
                            "A senha deve ter no mínimo " + Cliente.TAMANHO_MINIMO_SENHA + " caracteres.");
                }
                if (!senha.equals(new String(campoConfirmacao.getPassword()))) {
                    throw new IllegalArgumentException("As senhas não conferem.");
                }
                titular = new Cliente(nome, cpf, senha, campoCelular.getText());
            } else {
                if (!titular.autenticar(senha)) {
                    throw new IllegalArgumentException("Senha inválida. Criação de conta cancelada.");
                }
                if (banco.clientePossuiConta(titular, tipo)) {
                    throw new IllegalArgumentException("Você já possui uma conta desse tipo.");
                }
            }

            ContaBancaria nova = corrente ? banco.abrirContaCorrente(titular) : banco.abrirContaPoupanca(titular);
            if (nova == null) {
                throw new IllegalArgumentException("Você já possui uma conta desse tipo.");
            }

            resultado = nova;
            dispose();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Criar conta", JOptionPane.ERROR_MESSAGE);
        }
    }
}
