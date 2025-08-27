package br.com.trabalhofinal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Biblioteca {
    private static final int MAX_LIVROS = 100;
    private static final int MAX_ESTUDANTES = 50;
    private Livro[] livros = new Livro[MAX_LIVROS];
    private Estudante[] estudantes = new Estudante[MAX_ESTUDANTES];
    private int qtdLivros = 0;
    private int qtdEstudantes = 0;

    private File arquivoLivros = new File("livros.txt");
    private File arquivoEstudantes = new File("estudantes.txt");

    public void carregarDados() {
        carregarLivros();
        carregarEstudantes();
    }

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

    public void salvarDados() {
        salvarLivros();
        salvarEstudantes();
    }

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

    public boolean adicionarLivro(Livro livro) {
        if (qtdLivros >= MAX_LIVROS) {
            return false;
        }
        livros[qtdLivros++] = livro;
        return true;
    }

    public String listarLivros() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < qtdLivros; i++) {
            sb.append(livros[i].toString()).append("\n");
        }
        return sb.toString();
    }

    public boolean adicionarEstudante(Estudante estudante) {
        if (qtdEstudantes >= MAX_ESTUDANTES) {
            return false;
        }
        estudantes[qtdEstudantes++] = estudante;
        return true;
    }

    public String listarEstudantes() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < qtdEstudantes; i++) {
            sb.append(estudantes[i].toString()).append("\n");
        }
        return sb.toString();
    }

    private Livro buscarLivro(String codigo) {
        for (int i = 0; i < qtdLivros; i++) {
            if (livros[i].getCodigo().equals(codigo)) {
                return livros[i];
            }
        }
        return null;
    }

    private Estudante buscarEstudante(String ra) {
        for (int i = 0; i < qtdEstudantes; i++) {
            if (estudantes[i].getRa().equals(ra)) {
                return estudantes[i];
            }
        }
        return null;
    }

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
        if (!estudante.emprestarLivro(codigo)) {
            return "Estudante já possui 3 livros";
        }
        livro.emprestar(ra);
        return "Empréstimo realizado";
    }

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
