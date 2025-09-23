package br.com.trabalhofinal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável por gerenciar livros e estudantes da biblioteca.
 */
public class Biblioteca {
    // Limites máximos de cadastros, definidos para simplificar a implementação, já
    // que estamos usando txt
    private static final int MAX_LIVROS = 30;
    private static final int MAX_ESTUDANTES = 10;

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
            return; // Nada a carregar
        }
        try (BufferedReader br = new BufferedReader(new FileReader(arquivoLivros))) {
            String line;
            while ((line = br.readLine()) != null && qtdLivros < MAX_LIVROS) { // Lê linha a linha, até o fim do arquivo
                                                                               // ou atingir o limite
                Livro l = Livro.fromFileString(line); // Converte a linha em um objeto Livro
                if (l != null) {
                    livros[qtdLivros++] = l;
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // imprime no console o rastreamento da pilha, arquivo não encontrado, etc.
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
        // Se já atingiu o limite, não adiciona
        if (qtdLivros >= MAX_LIVROS) {
            return false;
        }
        // Se já existe um livro com o mesmo código, não adiciona
        if (buscarLivro(livro.getCodigo()) != null) {
            return false;
        }
        // Adiciona o livro no array e incrementa o contador
        livros[qtdLivros++] = livro;
        return true; // Deu certo
    }

    /**
     * Retorna representação textual dos livros cadastrados.
     */
    public String listarLivros() {
        // Se não tem livros, retorna a mensagem
        if (qtdLivros == 0) {
            return "Nenhum livro cadastrado.";
        }

        // StringBuilder para montar o texto da lista
        StringBuilder sb = new StringBuilder();

        // Percorre todos os livros e adiciona na String
        for (int i = 0; i < qtdLivros; i++) {
            sb.append(livros[i].toString()).append("\n");
        }

        // Retorna a lista sem a quebra de linha no final
        return sb.toString().trim();
    }

    /**
     * Cria e devolve um novo vetor só com os livros já cadastrados.
     * Assim, a interface recebe apenas os livros válidos,
     * sem ver os espaços vazios do array interno.
     */
    public Livro[] getLivros() {
        Livro[] resultado = new Livro[qtdLivros];
        System.arraycopy(livros, 0, resultado, 0, qtdLivros);
        return resultado;
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
     * Retorna somente os livros livres para novos empréstimos.
     */
    public Livro[] obterLivrosDisponiveis() {
        List<Livro> disponiveis = new ArrayList<>();
        for (int i = 0; i < qtdLivros; i++) {
            if (!livros[i].isEmprestado()) {
                disponiveis.add(livros[i]);
            }
        }
        return disponiveis.toArray(new Livro[0]);
    }

    /**
     * Retorna os livros que estão emprestados atualmente.
     */
    public Livro[] obterLivrosEmprestados() {
        List<Livro> emprestados = new ArrayList<>();
        for (int i = 0; i < qtdLivros; i++) {
            if (livros[i].isEmprestado()) {
                emprestados.add(livros[i]);
            }
        }
        return emprestados.toArray(new Livro[0]);
    }

    /**
     * Atualiza os dados de um livro já cadastrado.
     */
    public boolean atualizarLivro(String codigo, String titulo, String autor, int ano, String editora) {
        Livro livro = buscarLivro(codigo);
        if (livro == null) {
            return false;
        }
        livro.setTitulo(titulo);
        livro.setAutor(autor);
        livro.setAno(ano);
        livro.setEditora(editora);
        return true;
    }

    /**
     * Remove um livro do acervo se ele não estiver emprestado.
     */
    public boolean removerLivro(String codigo) {
        for (int i = 0; i < qtdLivros; i++) {
            if (livros[i].getCodigo().equals(codigo)) {
                if (livros[i].isEmprestado()) {
                    return false;
                }
                for (int j = i; j < qtdLivros - 1; j++) {
                    livros[j] = livros[j + 1];
                }
                livros[--qtdLivros] = null;
                return true;
            }
        }
        return false;
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
     * Atualiza os dados principais de um estudante.
     */
    public boolean atualizarEstudante(String ra, String curso, int periodo, String nome) {
        Estudante estudante = buscarEstudante(ra);
        if (estudante == null) {
            return false;
        }
        estudante.setCurso(curso);
        estudante.setPeriodo(periodo);
        estudante.setNome(nome);
        return true;
    }

    /**
     * Exclui um estudante caso não possua livros emprestados.
     */
    public boolean removerEstudante(String ra) {
        for (int i = 0; i < qtdEstudantes; i++) {
            if (estudantes[i].getRa().equals(ra)) {
                if (estudantes[i].getQuantidadeLivros() > 0) {
                    return false;
                }
                for (int j = i; j < qtdEstudantes - 1; j++) {
                    estudantes[j] = estudantes[j + 1];
                }
                estudantes[--qtdEstudantes] = null;
                return true;
            }
        }
        return false;
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

    /**
     * Entrega os estudantes cadastrados em formato de vetor.
     */
    public Estudante[] getEstudantes() {
        Estudante[] resultado = new Estudante[qtdEstudantes];
        System.arraycopy(estudantes, 0, resultado, 0, qtdEstudantes);
        return resultado;
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

    /**
     * Torna o método de busca de livro acessível para a interface.
     */
    public Livro obterLivro(String codigo) {
        return buscarLivro(codigo);
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
     * Torna a busca de estudantes disponível para outras classes.
     */
    public Estudante obterEstudante(String ra) {
        return buscarEstudante(ra);
    }

    /**
     * Recupera os livros emprestados para um determinado RA.
     */
    public Livro[] obterLivrosEmprestadosPorEstudante(String ra) {
        List<Livro> resultado = new ArrayList<>();
        for (int i = 0; i < qtdLivros; i++) {
            if (livros[i].isEmprestado() && livros[i].getRaEstudante().equals(ra)) {
                resultado.add(livros[i]);
            }
        }
        return resultado.toArray(new Livro[0]);
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
