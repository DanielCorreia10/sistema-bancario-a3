package gui;

import cliente.Cliente;
import contabancaria.Banco;
import contabancaria.ContaBancaria;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/** Janela principal: menu do CRUD de contas e listagem em tabela. */
public class TelaPrincipal extends JFrame {

    private static final String[] COLUNAS = {
        "Nome da conta", "Tipo", "CPF", "Celular", "Conta", "Agência", "Saldo"
    };

    private final Banco banco;
    private final DefaultTableModel modelo = new DefaultTableModel(COLUNAS, 0) {
        @Override
        public boolean isCellEditable(int linha, int coluna) {
            return false;
        }
    };
    private final JTable tabela = new JTable(modelo);
    private final JLabel status = new JLabel(" ");

    public TelaPrincipal(Banco banco) {
        super("Sistema Bancário");
        this.banco = banco;

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel titulo = new JLabel("=== Seja bem-vindo ===");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 18f));
        titulo.setBorder(new EmptyBorder(10, 12, 0, 12));

        JButton criar = new JButton("Criar conta");
        JButton consultar = new JButton("Consultar conta");
        JButton alterar = new JButton("Alterar dados");
        JButton excluir = new JButton("Excluir conta");
        JButton acessar = new JButton("Acessar conta");
        JButton sair = new JButton("Sair");

        criar.addActionListener(e -> criarConta());
        consultar.addActionListener(e -> consultarConta());
        alterar.addActionListener(e -> alterarDados());
        excluir.addActionListener(e -> excluirConta());
        acessar.addActionListener(e -> acessarConta());
        sair.addActionListener(e -> dispose());

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        botoes.add(criar);
        botoes.add(consultar);
        botoes.add(alterar);
        botoes.add(excluir);
        botoes.add(acessar);
        botoes.add(sair);

        JPanel topo = new JPanel(new BorderLayout());
        topo.add(titulo, BorderLayout.NORTH);
        topo.add(botoes, BorderLayout.CENTER);

        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.setRowHeight(24);
        tabela.getTableHeader().setReorderingAllowed(false);

        JPanel centro = new JPanel(new BorderLayout());
        centro.setBorder(new EmptyBorder(0, 12, 0, 12));
        centro.add(new JLabel("Contas cadastradas (selecione uma linha para consultar, alterar ou excluir):"),
                BorderLayout.NORTH);
        centro.add(new JScrollPane(tabela), BorderLayout.CENTER);

        status.setBorder(new EmptyBorder(6, 12, 8, 12));

        getContentPane().add(topo, BorderLayout.NORTH);
        getContentPane().add(centro, BorderLayout.CENTER);
        getContentPane().add(status, BorderLayout.SOUTH);

        atualizarTabela();
        setSize(940, 500);
        setLocationRelativeTo(null);
    }

    // ---------------------------------------------------------------- Create

    private void criarConta() {
        ContaBancaria nova = DialogoNovaConta.mostrar(this, banco);
        if (nova == null) {
            return;
        }
        atualizarTabela();
        status.setText("Conta " + Formatos.conta(nova.getConta()) + " criada com sucesso.");

        JOptionPane.showMessageDialog(this,
                "Sua conta foi criada com sucesso, veja seus dados abaixo:\n\n" + Formatos.detalhes(nova),
                "Conta aberta", JOptionPane.INFORMATION_MESSAGE);
    }

    // ------------------------------------------------------------------ Read

    private void consultarConta() {
        ContaBancaria conta = escolherContaAlvo("consultar");
        if (conta != null) {
            JOptionPane.showMessageDialog(this, Formatos.detalhes(conta), "Consulta de conta",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void atualizarTabela() {
        modelo.setRowCount(0);

        List<ContaBancaria> contas = banco.getContas();
        for (ContaBancaria conta : contas) {
            Cliente titular = conta.getTitular();
            modelo.addRow(new Object[] {
                conta.getNome(),
                conta.getTipoConta(),
                Formatos.cpf(titular.getCpf()),
                Formatos.celular(conta.getCelular()),
                Formatos.conta(conta.getConta()),
                Formatos.agencia(conta.getAgencia()),
                Formatos.moeda(conta.getSaldo())
            });
        }

        if (contas.isEmpty()) {
            status.setText("Nenhuma conta cadastrada.");
        }
    }

    // ---------------------------------------------------------------- Update

    private void alterarDados() {
        ContaBancaria conta = escolherContaAlvo("alterar");
        if (conta == null) {
            return;
        }

        String[] opcoes = {"Nome da conta", "Celular", "Cancelar"};
        int escolha = JOptionPane.showOptionDialog(this, "O que deseja alterar?", "Alterar dados",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);

        if (escolha == 0) {
            String novoNome = pedirTexto("Informe o novo nome da conta:", "Alterar nome", conta.getNome());
            if (novoNome == null) {
                return;
            }
            if (novoNome.isEmpty()) {
                erro("O nome não pode ficar vazio.");
                return;
            }
            banco.alterarNomeConta(conta.getConta(), novoNome);
            status.setText("Nome da conta alterado com sucesso!");
        } else if (escolha == 1) {
            String novoCelular = pedirTexto("Informe o celular com DDD (ex.: 11912345678):", "Alterar celular",
                    conta.getCelular());
            if (novoCelular == null) {
                return;
            }
            if (!Cliente.celularValido(novoCelular)) {
                erro("Celular inválido. Digite o DDD e o número, 11 dígitos no total.");
                return;
            }
            banco.alterarCelular(conta.getConta(), novoCelular);
            status.setText("Celular alterado com sucesso!");
        } else {
            return;
        }

        atualizarTabela();
    }

    // ---------------------------------------------------------------- Delete

    private void excluirConta() {
        ContaBancaria conta = escolherContaAlvo("excluir");
        if (conta == null) {
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Deseja realmente remover a conta " + Formatos.conta(conta.getConta())
                        + " (" + conta.getNome() + ")?",
                "Excluir conta", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirmacao != JOptionPane.YES_OPTION) {
            status.setText("Operação cancelada.");
            return;
        }

        if (banco.removerConta(conta.getConta())) {
            status.setText("Conta removida com sucesso!");
        } else {
            status.setText("Conta não encontrada.");
        }
        atualizarTabela();
    }

    // ------------------------------------------------------- Acesso à conta

    private void acessarConta() {
        ContaBancaria conta = DialogoLogin.mostrar(this, banco);
        if (conta == null) {
            return;
        }
        new TelaConta(this, conta).setVisible(true);
        atualizarTabela();
    }

    // --------------------------------------------------------------- Apoio

    /** Usa a linha selecionada na tabela ou, se não houver, pede o número da conta. */
    private ContaBancaria escolherContaAlvo(String acao) {
        int linha = tabela.getSelectedRow();
        if (linha >= 0) {
            return banco.getContas().get(linha);
        }

        String numero = pedirTexto("Informe o número da conta que deseja " + acao
                + "\n(ou selecione uma linha da tabela):", "Selecionar conta", "");
        if (numero == null) {
            return null;
        }

        ContaBancaria conta = banco.buscarConta(numero);
        if (conta == null) {
            erro("Conta não encontrada.");
        }
        return conta;
    }

    /** Devolve o texto digitado (sem espaços nas pontas) ou null se o usuário cancelar. */
    private String pedirTexto(String mensagem, String titulo, String valorInicial) {
        Object texto = JOptionPane.showInputDialog(this, mensagem, titulo, JOptionPane.QUESTION_MESSAGE,
                null, null, valorInicial);
        return texto == null ? null : texto.toString().trim();
    }

    private void erro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Atenção", JOptionPane.ERROR_MESSAGE);
    }
}
