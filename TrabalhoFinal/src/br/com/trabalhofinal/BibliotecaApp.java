package br.com.trabalhofinal;

import javax.swing.JOptionPane;

public class BibliotecaApp {
    public static void main(String[] args) {
        Biblioteca biblioteca = new Biblioteca();
        biblioteca.carregarDados();
        String opcao;
        do {
            opcao = JOptionPane.showInputDialog(null,
                "1 - Adicionar Livro\n" +
                "2 - Listar Livros\n" +
                "3 - Adicionar Estudante\n" +
                "4 - Listar Estudantes\n" +
                "5 - Emprestar Livro\n" +
                "6 - Devolver Livro\n" +
                "0 - Sair", "Biblioteca", JOptionPane.QUESTION_MESSAGE);
            if (opcao == null) {
                break;
            }
            switch (opcao) {
                case "1":
                    adicionarLivro(biblioteca);
                    break;
                case "2":
                    JOptionPane.showMessageDialog(null, biblioteca.listarLivros());
                    break;
                case "3":
                    adicionarEstudante(biblioteca);
                    break;
                case "4":
                    JOptionPane.showMessageDialog(null, biblioteca.listarEstudantes());
                    break;
                case "5":
                    emprestarLivro(biblioteca);
                    break;
                case "6":
                    devolverLivro(biblioteca);
                    break;
                case "0":
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opção inválida");
            }
        } while (!"0".equals(opcao));
        biblioteca.salvarDados();
    }

    private static void adicionarLivro(Biblioteca biblioteca) {
        String codigo = JOptionPane.showInputDialog("Código do livro:");
        String titulo = JOptionPane.showInputDialog("Título:");
        String autor = JOptionPane.showInputDialog("Autor:");
        String anoStr = JOptionPane.showInputDialog("Ano de publicação:");
        String editora = JOptionPane.showInputDialog("Editora:");
        int ano = Integer.parseInt(anoStr);
        Livro livro = new Livro(codigo, titulo, autor, ano, editora);
        if (biblioteca.adicionarLivro(livro)) {
            JOptionPane.showMessageDialog(null, "Livro adicionado");
        } else {
            JOptionPane.showMessageDialog(null, "Limite de livros atingido");
        }
    }

    private static void adicionarEstudante(Biblioteca biblioteca) {
        String curso = JOptionPane.showInputDialog("Curso:");
        String periodoStr = JOptionPane.showInputDialog("Período:");
        String nome = JOptionPane.showInputDialog("Nome:");
        String ra = JOptionPane.showInputDialog("RA:");
        int periodo = Integer.parseInt(periodoStr);
        Estudante estudante = new Estudante(curso, periodo, nome, ra);
        if (biblioteca.adicionarEstudante(estudante)) {
            JOptionPane.showMessageDialog(null, "Estudante adicionado");
        } else {
            JOptionPane.showMessageDialog(null, "Limite de estudantes atingido");
        }
    }

    private static void emprestarLivro(Biblioteca biblioteca) {
        String codigo = JOptionPane.showInputDialog("Código do livro:");
        String ra = JOptionPane.showInputDialog("RA do estudante:");
        String resultado = biblioteca.emprestarLivro(codigo, ra);
        JOptionPane.showMessageDialog(null, resultado);
    }

    private static void devolverLivro(Biblioteca biblioteca) {
        String codigo = JOptionPane.showInputDialog("Código do livro a devolver:");
        String resultado = biblioteca.devolverLivro(codigo);
        JOptionPane.showMessageDialog(null, resultado);
    }
}
