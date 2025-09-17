package br.com.trabalhofinal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

/**
 * Interface principal da aplicação. Organiza listagens, buscas e ações
 * relacionadas a livros, estudantes e empréstimos.
 */
public class BibliotecaUI extends JFrame {

    private final Biblioteca biblioteca;

    private DefaultTableModel livrosModel;
    private JTable livrosTable;
    private JTextField buscarLivrosField;

    private DefaultTableModel estudantesModel;
    private JTable estudantesTable;
    private JTextField buscarEstudantesField;

    private DefaultTableModel emprestimosModel;
    private JTable emprestimosTable;
    private JTextField buscarEmprestimosField;
    private JLabel resumoAlunoLabel;
    private JButton emprestarAlunoButton;
    private Estudante alunoSelecionado;

    public BibliotecaUI(Biblioteca biblioteca) {
        this.biblioteca = biblioteca;
        setTitle("Biblioteca Acadêmica");
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setSize(960, 640);

        adicionarEventosDeFechamento();
        setContentPane(criarConteudoPrincipal());

        atualizarTabelaLivros("");
        atualizarTabelaEstudantes("");
        atualizarTabelaEmprestimos(null);
        atualizarResumoAluno();
    }

    /**
     * Solicita confirmação antes de encerrar a aplicação e salva os dados.
     */
    private void adicionarEventosDeFechamento() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int opcao = JOptionPane.showConfirmDialog(
                        BibliotecaUI.this,
                        "Deseja sair da biblioteca?",
                        getTitle(),
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );
                if (opcao == JOptionPane.YES_OPTION) {
                    biblioteca.salvarDados();
                    JOptionPane.showMessageDialog(
                            BibliotecaUI.this,
                            "Dados salvos com sucesso. Até logo!",
                            getTitle(),
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    dispose();
                }
            }
        });
    }

    /**
     * Monta o tabbed pane com as principais seções da biblioteca.
     */
    private JComponent criarConteudoPrincipal() {
        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Livros", criarPainelLivros());
        abas.addTab("Estudantes", criarPainelEstudantes());
        abas.addTab("Empréstimos", criarPainelEmprestimos());
        return abas;
    }

    private JPanel criarPainelLivros() {
        JPanel painel = new JPanel(new BorderLayout(8, 8));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topo.add(new JLabel("Pesquisar:"));
        buscarLivrosField = new JTextField(20);
        topo.add(buscarLivrosField);

        JButton btnPesquisar = new JButton("Pesquisar");
        btnPesquisar.addActionListener(e -> atualizarTabelaLivros(buscarLivrosField.getText()));
        topo.add(btnPesquisar);

        JButton btnLimpar = new JButton("Limpar");
        btnLimpar.addActionListener(e -> {
            buscarLivrosField.setText("");
            atualizarTabelaLivros("");
        });
        topo.add(btnLimpar);

        JButton btnNovo = new JButton("Novo livro");
        btnNovo.addActionListener(e -> abrirDialogLivro(null));
        topo.add(btnNovo);

        painel.add(topo, BorderLayout.NORTH);

        livrosModel = new DefaultTableModel(new Object[]{
                "Código", "Título", "Autor", "Ano", "Editora", "Status", "Editar", "Excluir"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column >= 6;
            }
        };

        livrosTable = new JTable(livrosModel);
        livrosTable.setRowHeight(28);
        livrosTable.getTableHeader().setReorderingAllowed(false);
        livrosTable.setFillsViewportHeight(true);

        JScrollPane scroll = new JScrollPane(livrosTable);
        painel.add(scroll, BorderLayout.CENTER);

        Action editarLivroAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int linha = Integer.parseInt(e.getActionCommand());
                String codigo = (String) livrosModel.getValueAt(linha, 0);
                abrirDialogLivro(biblioteca.obterLivro(codigo));
            }
        };

        Action removerLivroAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int linha = Integer.parseInt(e.getActionCommand());
                String codigo = (String) livrosModel.getValueAt(linha, 0);
                removerLivro(codigo);
            }
        };

        configurarColunasDeAcao(livrosTable, "Editar", editarLivroAction);
        configurarColunasDeAcao(livrosTable, "Excluir", removerLivroAction);

        buscarLivrosField.addActionListener(e -> atualizarTabelaLivros(buscarLivrosField.getText()));

        return painel;
    }

    private JPanel criarPainelEstudantes() {
        JPanel painel = new JPanel(new BorderLayout(8, 8));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topo.add(new JLabel("Pesquisar:"));
        buscarEstudantesField = new JTextField(20);
        topo.add(buscarEstudantesField);

        JButton btnPesquisar = new JButton("Pesquisar");
        btnPesquisar.addActionListener(e -> atualizarTabelaEstudantes(buscarEstudantesField.getText()));
        topo.add(btnPesquisar);

        JButton btnLimpar = new JButton("Limpar");
        btnLimpar.addActionListener(e -> {
            buscarEstudantesField.setText("");
            atualizarTabelaEstudantes("");
        });
        topo.add(btnLimpar);

        JButton btnNovo = new JButton("Novo estudante");
        btnNovo.addActionListener(e -> abrirDialogEstudante(null));
        topo.add(btnNovo);

        painel.add(topo, BorderLayout.NORTH);

        estudantesModel = new DefaultTableModel(new Object[]{
                "Curso", "Período", "Nome", "RA", "Livros", "Editar", "Excluir"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column >= 5;
            }
        };

        estudantesTable = new JTable(estudantesModel);
        estudantesTable.setRowHeight(28);
        estudantesTable.getTableHeader().setReorderingAllowed(false);
        estudantesTable.setFillsViewportHeight(true);

        JScrollPane scroll = new JScrollPane(estudantesTable);
        painel.add(scroll, BorderLayout.CENTER);

        Action editarEstudanteAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int linha = Integer.parseInt(e.getActionCommand());
                String ra = (String) estudantesModel.getValueAt(linha, 3);
                abrirDialogEstudante(biblioteca.obterEstudante(ra));
            }
        };

        Action removerEstudanteAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int linha = Integer.parseInt(e.getActionCommand());
                String ra = (String) estudantesModel.getValueAt(linha, 3);
                removerEstudante(ra);
            }
        };

        configurarColunasDeAcao(estudantesTable, "Editar", editarEstudanteAction);
        configurarColunasDeAcao(estudantesTable, "Excluir", removerEstudanteAction);

        buscarEstudantesField.addActionListener(e -> atualizarTabelaEstudantes(buscarEstudantesField.getText()));

        return painel;
    }

    private JPanel criarPainelEmprestimos() {
        JPanel painel = new JPanel(new BorderLayout(8, 8));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topo.add(new JLabel("Aluno (nome ou RA):"));
        buscarEmprestimosField = new JTextField(20);
        topo.add(buscarEmprestimosField);

        // Campo de dica para o usuário
        JTextField dicaField = new JTextField("Pesquise um aluno ou RA antes de emprestar.");
        dicaField.setEditable(false);
        dicaField.setBorder(null);
        dicaField.setForeground(Color.GRAY);
        dicaField.setBackground(topo.getBackground());
        dicaField.setFont(buscarEmprestimosField.getFont().deriveFont(Font.ITALIC));
        topo.add(dicaField);
        

        JButton btnPesquisar = new JButton("Pesquisar aluno");
        btnPesquisar.addActionListener(e -> pesquisarAlunoEmprestimo());
        topo.add(btnPesquisar);

        JButton btnVerTodos = new JButton("Ver todos");
        btnVerTodos.addActionListener(e -> {
            alunoSelecionado = null;
            atualizarTabelaEmprestimos(null);
            atualizarResumoAluno();
        });
        topo.add(btnVerTodos);

        painel.add(topo, BorderLayout.NORTH);

        emprestimosModel = new DefaultTableModel(new Object[]{
                "Código", "Título", "Aluno", "RA", "Status", "Devolver"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };

        emprestimosTable = new JTable(emprestimosModel);
        emprestimosTable.setRowHeight(28);
        emprestimosTable.getTableHeader().setReorderingAllowed(false);
        emprestimosTable.setFillsViewportHeight(true);

        JScrollPane scroll = new JScrollPane(emprestimosTable);
        painel.add(scroll, BorderLayout.CENTER);

        Action devolverLivroAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int linha = Integer.parseInt(e.getActionCommand());
                String codigo = (String) emprestimosModel.getValueAt(linha, 0);
                devolverLivro(codigo);
            }
        };

        configurarColunasDeAcao(emprestimosTable, "Devolver", devolverLivroAction);

        buscarEmprestimosField.addActionListener(e -> pesquisarAlunoEmprestimo());

        JPanel rodape = new JPanel(new BorderLayout());
        resumoAlunoLabel = new JLabel();
        resumoAlunoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        rodape.add(resumoAlunoLabel, BorderLayout.CENTER);

        emprestarAlunoButton = new JButton("Emprestar livro");
        emprestarAlunoButton.addActionListener(e -> realizarEmprestimoAluno());
        rodape.add(emprestarAlunoButton, BorderLayout.EAST);

        painel.add(rodape, BorderLayout.SOUTH);

        return painel;
    }

    private void atualizarTabelaLivros(String filtro) {
        String termo = filtro == null ? "" : filtro.trim().toLowerCase();
        livrosModel.setRowCount(0);

        for (Livro livro : biblioteca.getLivros()) {
            if (termo.isEmpty() || correspondeAoLivro(livro, termo)) {
                String status = livro.isEmprestado()
                        ? "Emprestado (RA " + livro.getRaEstudante() + ")"
                        : "Disponível";
                livrosModel.addRow(new Object[]{
                        livro.getCodigo(),
                        livro.getTitulo(),
                        livro.getAutor(),
                        livro.getAno(),
                        livro.getEditora(),
                        status,
                        "Editar",
                        "Excluir"
                });
            }
        }
    }

    private boolean correspondeAoLivro(Livro livro, String termo) {
        String termoLower = termo.toLowerCase();
        return livro.getCodigo().toLowerCase().contains(termoLower)
                || livro.getTitulo().toLowerCase().contains(termoLower)
                || livro.getAutor().toLowerCase().contains(termoLower)
                || livro.getEditora().toLowerCase().contains(termoLower)
                || (livro.isEmprestado() && livro.getRaEstudante().toLowerCase().contains(termoLower));
    }

    private void atualizarTabelaEstudantes(String filtro) {
        String termo = filtro == null ? "" : filtro.trim().toLowerCase();
        estudantesModel.setRowCount(0);

        for (Estudante estudante : biblioteca.getEstudantes()) {
            if (termo.isEmpty() || correspondeAoEstudante(estudante, termo)) {
                StringJoiner joiner = new StringJoiner(", ");
                for (String codigo : estudante.getLivrosEmprestados()) {
                    if (codigo != null && !codigo.isEmpty()) {
                        joiner.add(codigo);
                    }
                }
                String livros = joiner.length() > 0 ? joiner.toString() : "Nenhum";
                estudantesModel.addRow(new Object[]{
                        estudante.getCurso(),
                        estudante.getPeriodo(),
                        estudante.getNome(),
                        estudante.getRa(),
                        livros,
                        "Editar",
                        "Excluir"
                });
            }
        }
    }

    private boolean correspondeAoEstudante(Estudante estudante, String termo) {
        String termoLower = termo.toLowerCase();
        if (estudante.getNome().toLowerCase().contains(termoLower)
                || estudante.getRa().toLowerCase().contains(termoLower)
                || estudante.getCurso().toLowerCase().contains(termoLower)) {
            return true;
        }
        for (String codigo : estudante.getLivrosEmprestados()) {
            if (codigo != null && codigo.toLowerCase().contains(termoLower)) {
                return true;
            }
        }
        return false;
    }

    private void atualizarTabelaEmprestimos(Estudante filtro) {
        emprestimosModel.setRowCount(0);
        Livro[] base = filtro == null
                ? biblioteca.obterLivrosEmprestados()
                : biblioteca.obterLivrosEmprestadosPorEstudante(filtro.getRa());

        for (Livro livro : base) {
            Estudante estudante = biblioteca.obterEstudante(livro.getRaEstudante());
            String nomeAluno = estudante != null ? estudante.getNome() : "-";
            emprestimosModel.addRow(new Object[]{
                    livro.getCodigo(),
                    livro.getTitulo(),
                    nomeAluno,
                    livro.getRaEstudante(),
                    "Emprestado",
                    "Devolver"
            });
        }
    }

    private void abrirDialogLivro(Livro livroExistente) {
        boolean edicao = livroExistente != null;
        String codigo = edicao ? livroExistente.getCodigo() : "";
        String titulo = edicao ? livroExistente.getTitulo() : "";
        String autor = edicao ? livroExistente.getAutor() : "";
        String anoTexto = edicao ? String.valueOf(livroExistente.getAno()) : "";
        String editora = edicao ? livroExistente.getEditora() : "";

        while (true) {
            JTextField codigoField = new JTextField(codigo);
            JTextField tituloField = new JTextField(titulo);
            JTextField autorField = new JTextField(autor);
            JTextField anoField = new JTextField(anoTexto);
            JTextField editoraField = new JTextField(editora);

            if (edicao) {
                codigoField.setEditable(false);
            }

            JPanel formulario = criarFormulario(
                    new String[]{"Código", "Título", "Autor", "Ano", "Editora"},
                    new JComponent[]{codigoField, tituloField, autorField, anoField, editoraField}
            );

            int resultado = JOptionPane.showConfirmDialog(
                    this,
                    formulario,
                    edicao ? "Editar livro" : "Cadastrar livro",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (resultado != JOptionPane.OK_OPTION) {
                return;
            }

            codigo = codigoField.getText().trim();
            titulo = tituloField.getText().trim();
            autor = autorField.getText().trim();
            anoTexto = anoField.getText().trim();
            editora = editoraField.getText().trim();

            if (codigo.isEmpty() || titulo.isEmpty() || autor.isEmpty() || anoTexto.isEmpty() || editora.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos.", getTitle(), JOptionPane.WARNING_MESSAGE);
                continue;
            }

            int ano;
            try {
                ano = Integer.parseInt(anoTexto);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Informe um ano válido.", getTitle(), JOptionPane.WARNING_MESSAGE);
                continue;
            }

            boolean sucesso = edicao
                    ? biblioteca.atualizarLivro(codigo, titulo, autor, ano, editora)
                    : biblioteca.adicionarLivro(new Livro(codigo, titulo, autor, ano, editora));

            if (sucesso) {
                JOptionPane.showMessageDialog(
                        this,
                        edicao ? "Livro atualizado com sucesso!" : "Livro cadastrado com sucesso!",
                        getTitle(),
                        JOptionPane.INFORMATION_MESSAGE
                );
                atualizarTodasAsListagens();
                return;
            }

            JOptionPane.showMessageDialog(
                    this,
                    edicao ? "Não foi possível atualizar o livro." : "Não foi possível cadastrar o livro. Verifique o código.",
                    getTitle(),
                    JOptionPane.WARNING_MESSAGE
            );

            if (edicao) {
                return;
            }
        }
    }

    private void abrirDialogEstudante(Estudante estudanteExistente) {
        boolean edicao = estudanteExistente != null;
        String curso = edicao ? estudanteExistente.getCurso() : "";
        String periodoTexto = edicao ? String.valueOf(estudanteExistente.getPeriodo()) : "";
        String nome = edicao ? estudanteExistente.getNome() : "";
        String ra = edicao ? estudanteExistente.getRa() : "";

        while (true) {
            JTextField cursoField = new JTextField(curso);
            JTextField periodoField = new JTextField(periodoTexto);
            JTextField nomeField = new JTextField(nome);
            JTextField raField = new JTextField(ra);

            if (edicao) {
                raField.setEditable(false);
            }

            JPanel formulario = criarFormulario(
                    new String[]{"Curso", "Período", "Nome", "RA"},
                    new JComponent[]{cursoField, periodoField, nomeField, raField}
            );

            int resultado = JOptionPane.showConfirmDialog(
                    this,
                    formulario,
                    edicao ? "Editar estudante" : "Cadastrar estudante",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (resultado != JOptionPane.OK_OPTION) {
                return;
            }

            curso = cursoField.getText().trim();
            periodoTexto = periodoField.getText().trim();
            nome = nomeField.getText().trim();
            ra = raField.getText().trim();

            if (curso.isEmpty() || periodoTexto.isEmpty() || nome.isEmpty() || ra.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos.", getTitle(), JOptionPane.WARNING_MESSAGE);
                continue;
            }

            int periodo;
            try {
                periodo = Integer.parseInt(periodoTexto);
                if (periodo <= 0) {
                    JOptionPane.showMessageDialog(this, "Informe um período válido.", getTitle(), JOptionPane.WARNING_MESSAGE);
                    continue;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Período deve ser numérico.", getTitle(), JOptionPane.WARNING_MESSAGE);
                continue;
            }

            boolean sucesso = edicao
                    ? biblioteca.atualizarEstudante(ra, curso, periodo, nome)
                    : biblioteca.adicionarEstudante(new Estudante(curso, periodo, nome, ra));

            if (sucesso) {
                JOptionPane.showMessageDialog(
                        this,
                        edicao ? "Estudante atualizado com sucesso!" : "Estudante cadastrado com sucesso!",
                        getTitle(),
                        JOptionPane.INFORMATION_MESSAGE
                );
                atualizarTodasAsListagens();
                return;
            }

            JOptionPane.showMessageDialog(
                    this,
                    edicao ? "Não foi possível atualizar o estudante." : "Não foi possível cadastrar o estudante. Verifique o RA.",
                    getTitle(),
                    JOptionPane.WARNING_MESSAGE
            );

            if (edicao) {
                return;
            }
        }
    }

    private void removerLivro(String codigo) {
        int opcao = JOptionPane.showConfirmDialog(
                this,
                "Deseja excluir o livro " + codigo + "?",
                getTitle(),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        if (opcao != JOptionPane.YES_OPTION) {
            return;
        }

        boolean sucesso = biblioteca.removerLivro(codigo);
        JOptionPane.showMessageDialog(
                this,
                sucesso ? "Livro removido com sucesso." : "Não é possível remover um livro emprestado.",
                getTitle(),
                sucesso ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE
        );
        atualizarTodasAsListagens();
    }

    private void removerEstudante(String ra) {
        Estudante estudante = biblioteca.obterEstudante(ra);
        if (estudante == null) {
            JOptionPane.showMessageDialog(this, "Estudante não encontrado.", getTitle(), JOptionPane.WARNING_MESSAGE);
            return;
        }
        int opcao = JOptionPane.showConfirmDialog(
                this,
                "Deseja excluir o estudante " + estudante.getNome() + "?",
                getTitle(),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        if (opcao != JOptionPane.YES_OPTION) {
            return;
        }

        boolean sucesso = biblioteca.removerEstudante(ra);
        JOptionPane.showMessageDialog(
                this,
                sucesso ? "Estudante removido com sucesso." : "Não é possível remover estudantes com livros emprestados.",
                getTitle(),
                sucesso ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE
        );
        atualizarTodasAsListagens();
    }

    private void pesquisarAlunoEmprestimo() {
        String termo = buscarEmprestimosField.getText().trim();
        if (termo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o nome ou RA do aluno.", getTitle(), JOptionPane.WARNING_MESSAGE);
            return;
        }
        String termoLower = termo.toLowerCase();
        List<Estudante> encontrados = new ArrayList<>();
        for (Estudante estudante : biblioteca.getEstudantes()) {
            if (estudante.getRa().equalsIgnoreCase(termo)
                    || estudante.getNome().toLowerCase().contains(termoLower)) {
                encontrados.add(estudante);
            }
        }

        if (encontrados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum estudante encontrado.", getTitle(), JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (encontrados.size() == 1) {
            alunoSelecionado = encontrados.get(0);
        } else {
            String[] opcoes = new String[encontrados.size()];
            for (int i = 0; i < opcoes.length; i++) {
                Estudante est = encontrados.get(i);
                opcoes[i] = est.getNome() + " - " + est.getRa();
            }
            String escolha = (String) JOptionPane.showInputDialog(
                    this,
                    "Selecione o estudante:",
                    getTitle(),
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    opcoes,
                    opcoes[0]
            );
            if (escolha == null) {
                return;
            }
            int indice = Arrays.asList(opcoes).indexOf(escolha);
            alunoSelecionado = encontrados.get(indice);
        }

        buscarEmprestimosField.setText(alunoSelecionado.getRa());
        atualizarTabelaEmprestimos(alunoSelecionado);
        atualizarResumoAluno();
    }

    private void realizarEmprestimoAluno() {
        if (alunoSelecionado == null) {
            return;
        }
        if (alunoSelecionado.getQuantidadeLivros() >= alunoSelecionado.getLimiteEmprestimos()) {
            JOptionPane.showMessageDialog(this, "O estudante atingiu o limite de empréstimos.", getTitle(), JOptionPane.WARNING_MESSAGE);
            return;
        }

        Livro[] disponiveis = biblioteca.obterLivrosDisponiveis();
        if (disponiveis.length == 0) {
            JOptionPane.showMessageDialog(this, "Nenhum livro disponível para empréstimo.", getTitle(), JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] opcoes = new String[disponiveis.length];
        for (int i = 0; i < disponiveis.length; i++) {
            opcoes[i] = disponiveis[i].getCodigo() + " - " + disponiveis[i].getTitulo();
        }
        JComboBox<String> combo = new JComboBox<>(opcoes);

        int resultado = JOptionPane.showConfirmDialog(
                this,
                combo,
                "Selecione um livro disponível",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );
        if (resultado != JOptionPane.OK_OPTION || combo.getSelectedIndex() < 0) {
            return;
        }

        Livro escolhido = disponiveis[combo.getSelectedIndex()];
        String mensagem = biblioteca.emprestarLivro(escolhido.getCodigo(), alunoSelecionado.getRa());
        JOptionPane.showMessageDialog(
                this,
                mensagem,
                getTitle(),
                "Empréstimo realizado".equalsIgnoreCase(mensagem)
                        ? JOptionPane.INFORMATION_MESSAGE
                        : JOptionPane.WARNING_MESSAGE
        );
        atualizarTodasAsListagens();
    }

    private void devolverLivro(String codigo) {
        int opcao = JOptionPane.showConfirmDialog(
                this,
                "Confirmar devolução do livro " + codigo + "?",
                getTitle(),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        if (opcao != JOptionPane.YES_OPTION) {
            return;
        }

        String mensagem = biblioteca.devolverLivro(codigo);
        JOptionPane.showMessageDialog(
                this,
                mensagem,
                getTitle(),
                "Livro devolvido".equalsIgnoreCase(mensagem)
                        ? JOptionPane.INFORMATION_MESSAGE
                        : JOptionPane.WARNING_MESSAGE
        );
        atualizarTodasAsListagens();
    }

    private void atualizarResumoAluno() {
        if (resumoAlunoLabel == null) {
            return;
        }
        if (alunoSelecionado == null) {
            resumoAlunoLabel.setText("Pesquise um aluno para detalhar seus empréstimos.");
            emprestarAlunoButton.setEnabled(false);
        } else {
            int atual = alunoSelecionado.getQuantidadeLivros();
            int limite = alunoSelecionado.getLimiteEmprestimos();
            String texto = String.format(
                    "Aluno %s (RA %s) possui %d de %d livros emprestados.",
                    alunoSelecionado.getNome(),
                    alunoSelecionado.getRa(),
                    atual,
                    limite
            );
            if (atual == limite) {
                texto += " <span style='color:red; font-weight:bold;'>&nbsp;LIMITE MÁXIMO ATINGIDO</span>";
                resumoAlunoLabel.setText("<html>" + texto + "</html>");
            } else {
                resumoAlunoLabel.setText(texto);
            }
            resumoAlunoLabel.setForeground(Color.BLACK);
            emprestarAlunoButton.setEnabled(atual < limite);
        }
    }

    private JPanel criarFormulario(String[] rotulos, JComponent[] campos) {
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

    private void configurarColunasDeAcao(JTable tabela, String coluna, Action acao) {
        tabela.getColumn(coluna).setCellRenderer(new ButtonRenderer());
        tabela.getColumn(coluna).setCellEditor(new ButtonEditor(tabela, acao));
        tabela.getColumn(coluna).setPreferredWidth(90);
    }

    private void atualizarTodasAsListagens() {
        atualizarTabelaLivros(buscarLivrosField.getText());
        atualizarTabelaEstudantes(buscarEstudantesField.getText());
        atualizarTabelaEmprestimos(alunoSelecionado);
        atualizarResumoAluno();
    }

    /**
     * Renderer simples que exibe botões nas colunas de ação.
     */
    private static class ButtonRenderer extends JButton implements TableCellRenderer {
        ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value == null ? "" : value.toString());
            return this;
        }
    }

    /**
     * Editor que aciona ações específicas ao clicar nos botões das tabelas.
     */
    private static class ButtonEditor extends AbstractCellEditor implements TableCellEditor, java.awt.event.ActionListener {
        private final JButton button = new JButton();
        private final JTable table;
        private final Action action;
        private int currentRow;

        ButtonEditor(JTable table, Action action) {
            this.table = table;
            this.action = action;
            button.setOpaque(true);
            button.setFocusable(false);
            button.addActionListener(this);
        }

        @Override
        public Object getCellEditorValue() {
            return button.getText();
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            currentRow = row;
            button.setText(value == null ? "" : value.toString());
            return button;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            fireEditingStopped();
            ActionEvent evento = new ActionEvent(table, ActionEvent.ACTION_PERFORMED, String.valueOf(currentRow));
            action.actionPerformed(evento);
        }
    }
}
