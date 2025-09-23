package br.com.trabalhofinal;

import java.awt.*;
import javax.swing.*;

/**
 * Ponto de entrada da aplicação. Responsável por exibir a splash screen
 * inicial e, após a carga, abrir a interface principal da biblioteca.
 */
public class BibliotecaApp {

    private BibliotecaApp() {
        // Evita instanciação
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> { // manda o código rodar na fila certa do Swing, assim a tela não trava nem buga.”
            configurarInterface();

            SplashScreenWindow splash = new SplashScreenWindow();
            splash.setVisible(true);

            Timer timer = new Timer(3000, event -> { // Simula tempo de carga
                splash.dispose(); // Fecha a splash screen
                iniciarAplicacao();
            });
            timer.setRepeats(false); // Executa apenas uma vez
            timer.start(); // Inicia o timer
        });
    }

    /**
     * Garante que o visual da aplicação acompanhe o do sistema operacional.
     */
    private static void configurarInterface() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Mantém o look and feel padrão em caso de erro.
        }
        UIManager.put("OptionPane.cancelButtonText", "Cancelar");
        UIManager.put("OptionPane.yesButtonText", "Sim");
        UIManager.put("OptionPane.noButtonText", "Não");
        UIManager.put("OptionPane.okButtonText", "OK");
    }

    /**
     * Instancia as classes de domínio e abre a interface gráfica principal.
     */
    private static void iniciarAplicacao() {
        Biblioteca biblioteca = new Biblioteca();
        biblioteca.carregarDados(); // Carrega dados salvos no txt

        BibliotecaUI ui = new BibliotecaUI(biblioteca);
        ui.setLocationRelativeTo(null); // Centraliza a janela
        ui.setVisible(true);
    }

    /**
     * Splash screen simples exibida durante a inicialização.
     */
    private static class SplashScreenWindow extends JWindow {
        SplashScreenWindow() {
            JPanel fundo = new JPanel(new BorderLayout());
            fundo.setBackground(new Color(18, 66, 114));
            fundo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JLabel titulo = new JLabel("Biblioteca Uniguaçu", SwingConstants.CENTER);
            titulo.setFont(new Font("Serif", Font.BOLD, 28));
            titulo.setForeground(Color.WHITE);

            JLabel subtitulo = new JLabel("Carregando recursos...", SwingConstants.CENTER);
            subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 16));
            subtitulo.setForeground(Color.WHITE);

            JProgressBar progresso = new JProgressBar();
            progresso.setIndeterminate(true);
            progresso.setBorderPainted(false);

            JPanel centro = new JPanel();
            centro.setOpaque(false);
            centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
            titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
            subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
            progresso.setAlignmentX(Component.CENTER_ALIGNMENT);

            centro.add(titulo);
            centro.add(Box.createRigidArea(new Dimension(0, 15)));
            centro.add(subtitulo);
            centro.add(Box.createRigidArea(new Dimension(0, 20)));
            centro.add(progresso);

            fundo.add(centro, BorderLayout.CENTER);

            setContentPane(fundo);
            setSize(380, 220);
            setLocationRelativeTo(null);
        }
    }
}
