package app;

import contabancaria.Banco;
import gui.TelaPrincipal;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignorada) {
                // mantém o visual padrão do Swing
            }
            new TelaPrincipal(new Banco()).setVisible(true);
        });
    }
}
