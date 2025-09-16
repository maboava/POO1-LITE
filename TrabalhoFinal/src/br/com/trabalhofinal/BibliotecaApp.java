package br.com.trabalhofinal;

import javax.swing.*;
import java.awt.*;

/**
 * Ponto de entrada da aplicação utilizando diálogos Swing.
 * A interface exibe formulários compactos para facilitar o cadastro,
 * listagem e gerenciamento dos livros e estudantes.
 */
public class BibliotecaApp {

    private static final String TITULO_APP = "Biblioteca Acadêmica";
    private static final String SEPARADOR_LISTA = "----------------------------------------";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BibliotecaApp::iniciarAplicacao);
    }

    private static void iniciarAplicacao() {
        configurarInterface();

        Biblioteca biblioteca = new Biblioteca();
        biblioteca.carregarDados();

        String[] opcoes = {
                "Cadastrar Livro",
                "Listar Todos os Livros",
                "Listar Livros Disponíveis",
                "Cadastrar Estudante",
                "Listar Estudantes",
                "Emprestar Livro",
                "Devolver Livro",
                "Sair"
        };

        boolean executando = true;
        while (executando) {
            int escolha = JOptionPane.showOptionDialog(
                    null,
                    "Escolha a ação desejada",
                    TITULO_APP,
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    opcoes,
                    opcoes[0]
            );

            if (escolha == JOptionPane.CLOSED_OPTION || escolha == opcoes.length - 1) {
                executando = false;
            } else {
                switch (escolha) {
                    case 0:
                        cadastrarLivro(biblioteca);
                        break;
                    case 1:
                        exibirLivros(biblioteca.listarLivros(), "Livros cadastrados");
                        break;
                    case 2:
                        exibirLivros(biblioteca.listarLivrosDisponiveis(), "Livros disponíveis");
                        break;
                    case 3:
                        cadastrarEstudante(biblioteca);
                        break;
                    case 4:
                        exibirEstudantes(biblioteca.listarEstudantes());
                        break;
                    case 5:
                        realizarEmprestimo(biblioteca);
                        break;
                    case 6:
                        realizarDevolucao(biblioteca);
                        break;
                    default:
                        break;
                }
            }
        }

        biblioteca.salvarDados();
        JOptionPane.showMessageDialog(
                null,
                "Dados salvos com sucesso. Até logo!",
                TITULO_APP,
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private static void configurarInterface() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Mantém o visual padrão caso não seja possível aplicar o look and feel do sistema.
        }
        UIManager.put("OptionPane.cancelButtonText", "Cancelar");
        UIManager.put("OptionPane.yesButtonText", "Sim");
        UIManager.put("OptionPane.noButtonText", "Não");
        UIManager.put("OptionPane.okButtonText", "OK");
    }

    private static void cadastrarLivro(Biblioteca biblioteca) {
        String codigo = "";
        String titulo = "";
        String autor = "";
        String anoTexto = "";
        String editora = "";

        while (true) {
            JTextField codigoField = new JTextField(codigo);
            JTextField tituloField = new JTextField(titulo);
            JTextField autorField = new JTextField(autor);
            JTextField anoField = new JTextField(anoTexto);
            JTextField editoraField = new JTextField(editora);

            JPanel painel = criarFormulario(
                    new String[]{"Código", "Título", "Autor", "Ano de publicação", "Editora"},
                    new JComponent[]{codigoField, tituloField, autorField, anoField, editoraField}
            );

            int resultado = JOptionPane.showConfirmDialog(
                    null,
                    painel,
                    "Cadastrar Livro",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (resultado != JOptionPane.OK_OPTION) {
                mostrarInformacao("Cadastro de livro cancelado.");
                return;
            }

            codigo = codigoField.getText().trim();
            titulo = tituloField.getText().trim();
            autor = autorField.getText().trim();
            anoTexto = anoField.getText().trim();
            editora = editoraField.getText().trim();

            if (codigo.isEmpty() || titulo.isEmpty() || autor.isEmpty() || anoTexto.isEmpty() || editora.isEmpty()) {
                mostrarAviso("Preencha todos os campos para cadastrar o livro.");
                continue;
            }

            int ano;
            try {
                ano = Integer.parseInt(anoTexto);
                if (ano <= 0) {
                    mostrarAviso("Informe um ano de publicação válido.");
                    continue;
                }
            } catch (NumberFormatException e) {
                mostrarAviso("Ano de publicação deve ser um número inteiro.");
                continue;
            }

            Livro livro = new Livro(codigo, titulo, autor, ano, editora);
            if (biblioteca.adicionarLivro(livro)) {
                mostrarInformacao("Livro cadastrado com sucesso!");
                return;
            }

            int tentarNovamente = JOptionPane.showConfirmDialog(
                    null,
                    "Não foi possível cadastrar o livro. Verifique se o código já existe ou se o limite foi atingido.\nDeseja tentar novamente?",
                    "Cadastrar Livro",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            if (tentarNovamente != JOptionPane.YES_OPTION) {
                return;
            }
        }
    }

    private static void cadastrarEstudante(Biblioteca biblioteca) {
        String curso = "";
        String periodoTexto = "";
        String nome = "";
        String ra = "";

        while (true) {
            JTextField cursoField = new JTextField(curso);
            JTextField periodoField = new JTextField(periodoTexto);
            JTextField nomeField = new JTextField(nome);
            JTextField raField = new JTextField(ra);

            JPanel painel = criarFormulario(
                    new String[]{"Curso", "Período", "Nome", "Registro Acadêmico (RA)"},
                    new JComponent[]{cursoField, periodoField, nomeField, raField}
            );

            int resultado = JOptionPane.showConfirmDialog(
                    null,
                    painel,
                    "Cadastrar Estudante",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (resultado != JOptionPane.OK_OPTION) {
                mostrarInformacao("Cadastro de estudante cancelado.");
                return;
            }

            curso = cursoField.getText().trim();
            periodoTexto = periodoField.getText().trim();
            nome = nomeField.getText().trim();
            ra = raField.getText().trim();

            if (curso.isEmpty() || periodoTexto.isEmpty() || nome.isEmpty() || ra.isEmpty()) {
                mostrarAviso("Preencha todos os campos para cadastrar o estudante.");
                continue;
            }

            int periodo;
            try {
                periodo = Integer.parseInt(periodoTexto);
                if (periodo <= 0) {
                    mostrarAviso("Informe um período válido (número inteiro positivo).");
                    continue;
                }
            } catch (NumberFormatException e) {
                mostrarAviso("Período deve ser um número inteiro.");
                continue;
            }

            Estudante estudante = new Estudante(curso, periodo, nome, ra);
            if (biblioteca.adicionarEstudante(estudante)) {
                mostrarInformacao("Estudante cadastrado com sucesso!");
                return;
            }

            int tentarNovamente = JOptionPane.showConfirmDialog(
                    null,
                    "Não foi possível cadastrar o estudante. Verifique se o RA já existe ou se o limite foi atingido.\nDeseja tentar novamente?",
                    "Cadastrar Estudante",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            if (tentarNovamente != JOptionPane.YES_OPTION) {
                return;
            }
        }
    }

    private static void realizarEmprestimo(Biblioteca biblioteca) {
        String codigo = "";
        String ra = "";

        while (true) {
            JTextField codigoField = new JTextField(codigo);
            JTextField raField = new JTextField(ra);

            JPanel painel = criarFormulario(
                    new String[]{"Código do livro", "RA do estudante"},
                    new JComponent[]{codigoField, raField}
            );

            int resultado = JOptionPane.showConfirmDialog(
                    null,
                    painel,
                    "Emprestar Livro",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (resultado != JOptionPane.OK_OPTION) {
                mostrarInformacao("Empréstimo cancelado.");
                return;
            }

            codigo = codigoField.getText().trim();
            ra = raField.getText().trim();

            if (codigo.isEmpty() || ra.isEmpty()) {
                mostrarAviso("Informe o código do livro e o RA do estudante.");
                continue;
            }

            String mensagem = biblioteca.emprestarLivro(codigo, ra);
            if ("Empréstimo realizado".equalsIgnoreCase(mensagem)) {
                mostrarInformacao(mensagem);
                return;
            }

            int tentarNovamente = JOptionPane.showConfirmDialog(
                    null,
                    mensagem + "\nDeseja tentar novamente?",
                    "Emprestar Livro",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            if (tentarNovamente != JOptionPane.YES_OPTION) {
                return;
            }
        }
    }

    private static void realizarDevolucao(Biblioteca biblioteca) {
        String codigo = "";

        while (true) {
            JTextField codigoField = new JTextField(codigo);

            JPanel painel = criarFormulario(
                    new String[]{"Código do livro"},
                    new JComponent[]{codigoField}
            );

            int resultado = JOptionPane.showConfirmDialog(
                    null,
                    painel,
                    "Devolver Livro",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (resultado != JOptionPane.OK_OPTION) {
                mostrarInformacao("Devolução cancelada.");
                return;
            }

            codigo = codigoField.getText().trim();
            if (codigo.isEmpty()) {
                mostrarAviso("Informe o código do livro para efetuar a devolução.");
                continue;
            }

            String mensagem = biblioteca.devolverLivro(codigo);
            if ("Livro devolvido".equalsIgnoreCase(mensagem)) {
                mostrarInformacao(mensagem);
                return;
            }

            int tentarNovamente = JOptionPane.showConfirmDialog(
                    null,
                    mensagem + "\nDeseja tentar novamente?",
                    "Devolver Livro",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            if (tentarNovamente != JOptionPane.YES_OPTION) {
                return;
            }
        }
    }

    private static void exibirLivros(String dados, String titulo) {
        if (dados == null || dados.trim().isEmpty()) {
            mostrarInformacao("Nenhum livro cadastrado.");
            return;
        }
        if (dados.startsWith("Nenhum")) {
            mostrarInformacao(dados);
            return;
        }
        exibirTextoComRolagem(titulo, formatarLivros(dados));
    }

    private static void exibirEstudantes(String dados) {
        if (dados == null || dados.trim().isEmpty()) {
            mostrarInformacao("Nenhum estudante cadastrado.");
            return;
        }
        if (dados.startsWith("Nenhum")) {
            mostrarInformacao(dados);
            return;
        }
        exibirTextoComRolagem("Estudantes cadastrados", formatarEstudantes(dados));
    }

    private static void exibirTextoComRolagem(String titulo, String conteudo) {
        JTextArea area = new JTextArea(conteudo);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        area.setMargin(new Insets(8, 8, 8, 8));

        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(480, 320));

        JOptionPane.showMessageDialog(null, scroll, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    private static JPanel criarFormulario(String[] rotulos, JComponent[] campos) {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (int i = 0; i < rotulos.length; i++) {
            GridBagConstraints gbcLabel = new GridBagConstraints();
            gbcLabel.gridx = 0;
            gbcLabel.gridy = i;
            gbcLabel.anchor = GridBagConstraints.WEST;
            gbcLabel.insets = new Insets(4, 8, 4, 8);
            painel.add(new JLabel(rotulos[i] + ":"), gbcLabel);

            GridBagConstraints gbcCampo = new GridBagConstraints();
            gbcCampo.gridx = 1;
            gbcCampo.gridy = i;
            gbcCampo.weightx = 1.0;
            gbcCampo.fill = GridBagConstraints.HORIZONTAL;
            gbcCampo.insets = new Insets(4, 8, 4, 8);
            painel.add(campos[i], gbcCampo);
        }

        return painel;
    }

    private static String formatarLivros(String dados) {
        StringBuilder sb = new StringBuilder();
        String[] linhas = dados.split("\\n");
        for (String linha : linhas) {
            if (linha == null || linha.trim().isEmpty()) {
                continue;
            }
            if (sb.length() > 0) {
                sb.append("\n").append(SEPARADOR_LISTA).append("\n");
            }
            String[] partes = linha.split(";", -1);
            if (partes.length >= 5) {
                sb.append("Código: ").append(partes[0]).append('\n');
                sb.append("Título: ").append(partes[1]).append('\n');
                sb.append("Autor: ").append(partes[2]).append('\n');
                sb.append("Ano: ").append(partes[3]).append('\n');
                sb.append("Editora: ").append(partes[4]).append('\n');
                if (partes.length >= 6) {
                    sb.append("Status: ").append(partes[5]).append('\n');
                }
            } else {
                sb.append(linha);
            }
        }
        return sb.toString().trim();
    }

    private static String formatarEstudantes(String dados) {
        StringBuilder sb = new StringBuilder();
        String[] linhas = dados.split("\\n");
        for (String linha : linhas) {
            if (linha == null || linha.trim().isEmpty()) {
                continue;
            }
            if (sb.length() > 0) {
                sb.append("\n").append(SEPARADOR_LISTA).append("\n");
            }
            String[] partes = linha.split(";", -1);
            if (partes.length >= 4) {
                sb.append("Nome: ").append(partes[2]).append('\n');
                sb.append("RA: ").append(partes[3]).append('\n');
                sb.append("Curso: ").append(partes[0]).append('\n');
                sb.append("Período: ").append(partes[1]).append('\n');
                if (partes.length >= 5) {
                    sb.append(partes[4]).append('\n');
                }
            } else {
                sb.append(linha);
            }
        }
        return sb.toString().trim();
    }

    private static void mostrarInformacao(String mensagem) {
        JOptionPane.showMessageDialog(null, mensagem, TITULO_APP, JOptionPane.INFORMATION_MESSAGE);
    }

    private static void mostrarAviso(String mensagem) {
        JOptionPane.showMessageDialog(null, mensagem, TITULO_APP, JOptionPane.WARNING_MESSAGE);
    }
}
