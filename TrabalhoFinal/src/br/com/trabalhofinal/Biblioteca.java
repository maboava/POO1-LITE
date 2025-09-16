package br.com.trabalhofinal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Classe responsável por gerenciar livros e estudantes da biblioteca.
 */
public class Biblioteca {
    // Limites máximos de cadastros
    private static final int MAX_LIVROS = 100;
    private static final int MAX_ESTUDANTES = 50;

    // Vetores que armazenam livros e estudantes
    private Livro[] livros = new Livro[MAX_LIVROS];
    private Estudante[] estudantes = new Estudante[MAX_ESTUDANTES];

    // Quantidades atuais de livros e estudantes
    private int qtdLivros = 0;
    private int qtdEstudantes = 0;

    // Arquivos utilizados para persistência dos dados
    private File arquivoLivros = new File("livros.txt");
    private File arquivoEstudantes = new File("estudantes.txt");

    /**
     * Carrega os dados salvos e, se estiver vazio, adiciona registros padrão.
     */
    public void carregarDados() {
        carregarLivros();
        carregarEstudantes();

        // Caso não existam dados, adiciona exemplos para facilitar testes
        if (qtdLivros == 0) {
            adicionarLivro(new Livro("L001", "Dom Casmurro", "Machado de Assis", 1899, "Garnier"));
            adicionarLivro(new Livro("L002", "Memórias Póstumas", "Machado de Assis", 1881, "Revista Brasileira"));
        }
        if (qtdEstudantes == 0) {
            adicionarEstudante(new Estudante("Computação", 3, "Ana Silva", "RA001"));
            adicionarEstudante(new Estudante("Engenharia", 2, "Carlos Souza", "RA002"));
        }
    }

    // Lê os livros do arquivo de persistência
    private void carregarLivros() {
        if (!arquivoLivros.exists()) {
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(arquivoLivros))) {
            String line;
            while ((line = br.readLine()) != null && qtdLivros < MAX_LIVROS) {
                Livro l = Livro.fromFileString(line);
                if (l != null) {
                    livros[qtdLivros++] = l;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Lê os estudantes do arquivo de persistência
    private void carregarEstudantes() {
        if (!arquivoEstudantes.exists()) {
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(arquivoEstudantes))) {
            String line;
            while ((line = br.readLine()) != null && qtdEstudantes < MAX_ESTUDANTES) {
                Estudante e = Estudante.fromFileString(line);
                if (e != null) {
                    estudantes[qtdEstudantes++] = e;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Salva os dados atuais de livros e estudantes nos arquivos.
     */
    public void salvarDados() {
        salvarLivros();
        salvarEstudantes();
    }

    // Persiste os livros em arquivo
    private void salvarLivros() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(arquivoLivros))) {
            for (int i = 0; i < qtdLivros; i++) {
                bw.write(livros[i].toFileString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Persiste os estudantes em arquivo
    private void salvarEstudantes() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(arquivoEstudantes))) {
            for (int i = 0; i < qtdEstudantes; i++) {
                bw.write(estudantes[i].toFileString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adiciona um livro ao acervo.
     */
    public boolean adicionarLivro(Livro livro) {
        if (qtdLivros >= MAX_LIVROS) {
            return false;
        }
        if (buscarLivro(livro.getCodigo()) != null) {
            return false;
        }
        livros[qtdLivros++] = livro;
        return true;
    }

    /**
     * Retorna representação textual dos livros cadastrados.
     */
    public String listarLivros() {
        if (qtdLivros == 0) {
            return "Nenhum livro cadastrado.";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < qtdLivros; i++) {
            sb.append(livros[i].toString()).append("\n");
        }
        return sb.toString().trim();
    }

    /**
     * Retorna apenas os livros que estão disponíveis para empréstimo.
     */
    public String listarLivrosDisponiveis() {
        boolean possuiDisponiveis = false;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < qtdLivros; i++) {
            if (!livros[i].isEmprestado()) {
                sb.append(livros[i].toString()).append("\n");
                possuiDisponiveis = true;
            }
        }
        return possuiDisponiveis ? sb.toString().trim() : "Nenhum livro disponível.";
    }

    /**
     * Adiciona um estudante ao cadastro.
     */
    public boolean adicionarEstudante(Estudante estudante) {
        if (qtdEstudantes >= MAX_ESTUDANTES) {
            return false;
        }
        if (buscarEstudante(estudante.getRa()) != null) {
            return false;
        }
        estudantes[qtdEstudantes++] = estudante;
        return true;
    }

    /**
     * Retorna representação textual dos estudantes cadastrados.
     */
    public String listarEstudantes() {
        if (qtdEstudantes == 0) {
            return "Nenhum estudante cadastrado.";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < qtdEstudantes; i++) {
            sb.append(estudantes[i].toString()).append("\n");
        }
        return sb.toString().trim();
    }

    // Procura um livro pelo código
    private Livro buscarLivro(String codigo) {
        for (int i = 0; i < qtdLivros; i++) {
            if (livros[i].getCodigo().equals(codigo)) {
                return livros[i];
            }
        }
        return null;
    }

    // Procura um estudante pelo RA
    private Estudante buscarEstudante(String ra) {
        for (int i = 0; i < qtdEstudantes; i++) {
            if (estudantes[i].getRa().equals(ra)) {
                return estudantes[i];
            }
        }
        return null;
    }

    /**
     * Empresta um livro para um estudante, se possível.
     */
    public String emprestarLivro(String codigo, String ra) {
        Livro livro = buscarLivro(codigo);
        if (livro == null) {
            return "Livro não encontrado";
        }
        if (livro.isEmprestado()) {
            return "Livro já emprestado";
        }
        Estudante estudante = buscarEstudante(ra);
        if (estudante == null) {
            return "Estudante não encontrado";
        }
        if (estudante.possuiLivro(codigo)) {
            return "Estudante já possui este livro";
        }
        if (!estudante.emprestarLivro(codigo)) {
            return "Estudante já possui 3 livros";
        }
        livro.emprestar(ra);
        return "Empréstimo realizado";
    }

    /**
     * Devolve um livro ao acervo.
     */
    public String devolverLivro(String codigo) {
        Livro livro = buscarLivro(codigo);
        if (livro == null) {
            return "Livro não encontrado";
        }
        if (!livro.isEmprestado()) {
            return "Livro não está emprestado";
        }
        Estudante estudante = buscarEstudante(livro.getRaEstudante());
        if (estudante != null) {
            estudante.devolverLivro(codigo);
        }
        livro.devolver();
        return "Livro devolvido";
    }
}
