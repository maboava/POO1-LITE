package br.com.trabalhofinal;

/**
 * Representa um livro disponível na biblioteca.
 */
public class Livro {
    // Dados básicos do livro
    private String codigo;
    private String titulo;
    private String autor;
    private int ano;
    private String editora;

    // Controle de empréstimo
    private boolean emprestado;
    private String raEstudante;

    public Livro(String codigo, String titulo, String autor, int ano, String editora) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.autor = autor;
        this.ano = ano;
        this.editora = editora;
        this.emprestado = false;
        this.raEstudante = "";
    }

    // Métodos de acesso aos atributos
    public String getCodigo() { return codigo; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public int getAno() { return ano; }
    public String getEditora() { return editora; }
    public boolean isEmprestado() { return emprestado; }
    public String getRaEstudante() { return raEstudante; }

    /**
     * Marca o livro como emprestado para o RA informado.
     */
    public void emprestar(String ra) {
        this.emprestado = true;
        this.raEstudante = ra;
    }

    /**
     * Devolve o livro ao acervo.
     */
    public void devolver() {
        this.emprestado = false;
        this.raEstudante = "";
    }

    @Override
    public String toString() {
        String status = emprestado ? "Emprestado para RA " + raEstudante : "Disponível";
        return codigo + ";" + titulo + ";" + autor + ";" + ano + ";" + editora + ";" + status;
    }

    // Representação utilizada para salvar em arquivo
    public String toFileString() {
        return codigo + ";" + titulo + ";" + autor + ";" + ano + ";" + editora + ";" + (emprestado ? raEstudante : "");
    }

    // Constrói um livro a partir de uma linha do arquivo
    public static Livro fromFileString(String line) {
        String[] parts = line.split(";");
        if (parts.length < 5) {
            return null;
        }
        Livro l = new Livro(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]), parts[4]);
        if (parts.length > 5 && !parts[5].isEmpty()) {
            l.emprestar(parts[5]);
        }
        return l;
    }
}
