package br.com.trabalhofinal;

/**
 * Representa um estudante que pode realizar empréstimos na biblioteca.
 */
public class Estudante {
    // Limite padrão de empréstimos por aluno
    private static final int LIMITE_EMPRESTIMOS = 3;

    // Dados pessoais do estudante
    private String curso;
    private int periodo;
    private String nome;
    private String ra;

    // Armazena os códigos dos livros emprestados (até 3)
    private String[] livrosEmprestados = new String[LIMITE_EMPRESTIMOS];

    public Estudante(String curso, int periodo, String nome, String ra) {
        this.curso = curso;
        this.periodo = periodo;
        this.nome = nome;
        this.ra = ra;
    }

    // Métodos de acesso
    public String getCurso() { return curso; }
    public int getPeriodo() { return periodo; }
    public String getNome() { return nome; }
    public String getRa() { return ra; }

    // Ajusta dados pessoais
    public void setCurso(String curso) { this.curso = curso; }
    public void setPeriodo(int periodo) { this.periodo = periodo; }
    public void setNome(String nome) { this.nome = nome; }

    /**
     * Registra o empréstimo de um livro caso haja vaga.
     */
    public boolean emprestarLivro(String codigoLivro) {
        if (possuiLivro(codigoLivro)) {
            return false;
        }
        for (int i = 0; i < livrosEmprestados.length; i++) {
            if (livrosEmprestados[i] == null) {
                livrosEmprestados[i] = codigoLivro;
                return true;
            }
        }
        return false;
    }

    /**
     * Remove o livro da lista de empréstimos.
     */
    public boolean devolverLivro(String codigoLivro) {
        for (int i = 0; i < livrosEmprestados.length; i++) {
            if (codigoLivro.equals(livrosEmprestados[i])) {
                livrosEmprestados[i] = null;
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica se o estudante possui o livro informado.
     */
    public boolean possuiLivro(String codigoLivro) {
        for (String codigo : livrosEmprestados) {
            if (codigoLivro.equals(codigo)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Informa quantos livros o estudante possui atualmente.
     */
    public int getQuantidadeLivros() {
        int count = 0;
        for (String codigo : livrosEmprestados) {
            if (codigo != null) {
                count++;
            }
        }
        return count;
    }

    /**
     * Retorna o limite máximo de empréstimos permitido para o estudante.
     */
    public int getLimiteEmprestimos() {
        return LIMITE_EMPRESTIMOS;
    }

    /**
     * Disponibiliza os códigos dos livros emprestados para a interface.
     */
    public String[] getLivrosEmprestados() {
        String[] copia = new String[livrosEmprestados.length];
        System.arraycopy(livrosEmprestados, 0, copia, 0, livrosEmprestados.length);
        return copia;
    }

    /**
     * Limite global disponível para reaproveitamento em outras classes.
     */
    public static int getLimiteEmprestimosPorEstudante() {
        return LIMITE_EMPRESTIMOS;
    }

    // Representação utilizada para salvar em arquivo
    public String toFileString() {
        StringBuilder sb = new StringBuilder();
        sb.append(curso).append(";").append(periodo).append(";")
          .append(nome).append(";").append(ra).append(";");
        for (int i = 0; i < livrosEmprestados.length; i++) {
            if (livrosEmprestados[i] != null) {
                sb.append(livrosEmprestados[i]);
            }
            sb.append(i < livrosEmprestados.length - 1 ? "," : "");
        }
        return sb.toString();
    }

    // Constrói um estudante a partir de uma linha do arquivo
    public static Estudante fromFileString(String line) {
        String[] parts = line.split(";");
        if (parts.length < 4) {
            return null;
        }
        Estudante e = new Estudante(parts[0], Integer.parseInt(parts[1]), parts[2], parts[3]);
        if (parts.length > 4 && !parts[4].isEmpty()) {
            String[] codigos = parts[4].split(",");
            for (int i = 0; i < codigos.length && i < e.livrosEmprestados.length; i++) {
                if (!codigos[i].isEmpty()) {
                    e.livrosEmprestados[i] = codigos[i];
                }
            }
        }
        return e;
    }

    @Override // significa que você está reescrevendo um método herdado para mudar o comportamento padrão dele.
    public String toString() {
        StringBuilder sb = new StringBuilder(); //sb é uma variável do tipo StringBuilder que é usada para construir uma string de forma eficiente.
        sb.append(curso).append(";").append(periodo).append(";")
          .append(nome).append(";").append(ra).append(";");
        sb.append("Livros: ");
        boolean possuiLivros = false;
        for (String codigo : livrosEmprestados) {
            if (codigo != null) {
                sb.append(codigo).append(" ");
                possuiLivros = true;
            }
        }
        if (!possuiLivros) {
            sb.append("Nenhum");
        }
        return sb.toString().trim();
    }
}
