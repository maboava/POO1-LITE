package br.com.trabalhofinal;

import java.util.Scanner;

/**
 * Ponto de entrada da aplicação em modo console.
 * A classe exibe um menu simples para que o usuário execute
 * as operações de cadastro, listagem, empréstimo e devolução
 * de livros utilizando apenas classes, arrays e arquivos texto.
 */
public class BibliotecaApp {

    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        Biblioteca biblioteca = new Biblioteca();
        biblioteca.carregarDados();

        int opcao;
        do {
            exibirMenu();
            opcao = lerInteiro("Escolha uma opção: ");

            switch (opcao) {
                case 1:
                    adicionarLivro(biblioteca);
                    break;
                case 2:
                    System.out.println("\n--- Lista de Livros ---");
                    System.out.println(biblioteca.listarLivros());
                    break;
                case 3:
                    System.out.println("\n--- Livros Disponíveis ---");
                    System.out.println(biblioteca.listarLivrosDisponiveis());
                    break;
                case 4:
                    adicionarEstudante(biblioteca);
                    break;
                case 5:
                    System.out.println("\n--- Lista de Estudantes ---");
                    System.out.println(biblioteca.listarEstudantes());
                    break;
                case 6:
                    emprestarLivro(biblioteca);
                    break;
                case 7:
                    devolverLivro(biblioteca);
                    break;
                case 0:
                    System.out.println("Encerrando o sistema...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
                    break;
            }
        } while (opcao != 0);

        biblioteca.salvarDados();
        System.out.println("Dados salvos com sucesso. Até logo!");
        SCANNER.close();
    }

    private static void exibirMenu() {
        System.out.println("\n====== Biblioteca ======");
        System.out.println("1 - Cadastrar Livro");
        System.out.println("2 - Listar Todos os Livros");
        System.out.println("3 - Listar Apenas Livros Disponíveis");
        System.out.println("4 - Cadastrar Estudante");
        System.out.println("5 - Listar Estudantes");
        System.out.println("6 - Emprestar Livro");
        System.out.println("7 - Devolver Livro");
        System.out.println("0 - Sair");
        System.out.println("========================");
    }

    private static void adicionarLivro(Biblioteca biblioteca) {
        System.out.println("\n--- Cadastro de Livro ---");
        String codigo = lerTexto("Código do livro: ");
        String titulo = lerTexto("Título: ");
        String autor = lerTexto("Autor: ");
        int ano = lerInteiro("Ano de publicação: ");
        String editora = lerTexto("Editora: ");

        Livro livro = new Livro(codigo, titulo, autor, ano, editora);
        if (biblioteca.adicionarLivro(livro)) {
            System.out.println("Livro cadastrado com sucesso!");
        } else {
            System.out.println("Não foi possível cadastrar o livro. Verifique se já existe outro com o mesmo código ou se o limite foi atingido.");
        }
    }

    private static void adicionarEstudante(Biblioteca biblioteca) {
        System.out.println("\n--- Cadastro de Estudante ---");
        String curso = lerTexto("Curso: ");
        int periodo = lerInteiro("Período: ");
        String nome = lerTexto("Nome: ");
        String ra = lerTexto("Registro Acadêmico (RA): ");

        Estudante estudante = new Estudante(curso, periodo, nome, ra);
        if (biblioteca.adicionarEstudante(estudante)) {
            System.out.println("Estudante cadastrado com sucesso!");
        } else {
            System.out.println("Não foi possível cadastrar o estudante. Verifique se o RA já foi utilizado ou se o limite foi atingido.");
        }
    }

    private static void emprestarLivro(Biblioteca biblioteca) {
        System.out.println("\n--- Empréstimo de Livro ---");
        String codigo = lerTexto("Código do livro: ");
        String ra = lerTexto("RA do estudante: ");
        System.out.println(biblioteca.emprestarLivro(codigo, ra));
    }

    private static void devolverLivro(Biblioteca biblioteca) {
        System.out.println("\n--- Devolução de Livro ---");
        String codigo = lerTexto("Código do livro: ");
        System.out.println(biblioteca.devolverLivro(codigo));
    }

    private static int lerInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = SCANNER.nextLine();
            try {
                return Integer.parseInt(entrada.trim());
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido. Digite um número inteiro.");
            }
        }
    }

    private static String lerTexto(String mensagem) {
        System.out.print(mensagem);
        return SCANNER.nextLine().trim();
    }
}
