package br.com.trabalhofinal;

import javax.swing.*;
import java.awt.*;

/**
 * Aplicação principal da biblioteca.
 * Responsável por exibir o menu e interagir com o usuário.
 */
public class BibliotecaApp {

    public static void main(String[] args) {
        Biblioteca biblioteca = new Biblioteca();
        biblioteca.carregarDados();

        // Opções exibidas como botões para facilitar o uso
        String[] opcoes = {
            "Adicionar Livro", "Listar Livros", "Adicionar Estudante",
            "Listar Estudantes", "Emprestar Livro", "Devolver Livro", "Sair"
        };
        int escolha;
        do {
            escolha = JOptionPane.showOptionDialog(
                    null,
                    "Selecione uma opção",
                    "Biblioteca",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    opcoes,
                    opcoes[0]
            );

            switch (escolha) {
                case 0:
                    adicionarLivro(biblioteca);
                    break;
                case 1:
                    exibirMensagem("Lista de Livros", biblioteca.listarLivros());
                    break;
                case 2:
                    adicionarEstudante(biblioteca);
                    break;
                case 3:
                    exibirMensagem("Lista de Estudantes", biblioteca.listarEstudantes());
                    break;
                case 4:
                    emprestarLivro(biblioteca);
                    break;
                case 5:
                    devolverLivro(biblioteca);
                    break;
                default:
                    // opção 6 ou janela fechada encerram o programa
                    break;
            }
        } while (escolha != 6 && escolha != JOptionPane.CLOSED_OPTION);

        biblioteca.salvarDados();
    }

    /**
     * Exibe uma mensagem em uma caixa com rolagem para melhor visualização.
     */
    private static void exibirMensagem(String titulo, String texto) {
        JTextArea area = new JTextArea(texto);
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(400, 300));
        JOptionPane.showMessageDialog(null, scroll, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Solicita os dados de um livro e o adiciona à biblioteca.
     */
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

    /**
     * Solicita os dados de um estudante e o adiciona à biblioteca.
     */
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

    /**
     * Solicita as informações necessárias para emprestar um livro.
     */
    private static void emprestarLivro(Biblioteca biblioteca) {
        String codigo = JOptionPane.showInputDialog("Código do livro:");
        String ra = JOptionPane.showInputDialog("RA do estudante:");
        String resultado = biblioteca.emprestarLivro(codigo, ra);
        JOptionPane.showMessageDialog(null, resultado);
    }

    /**
     * Solicita as informações necessárias para devolver um livro.
     */
    private static void devolverLivro(Biblioteca biblioteca) {
        String codigo = JOptionPane.showInputDialog("Código do livro a devolver:");
        String resultado = biblioteca.devolverLivro(codigo);
        JOptionPane.showMessageDialog(null, resultado);
    }
}
