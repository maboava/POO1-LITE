package br.com.trabalhofinal;

public class Estudante {
    private String curso;
    private int periodo;
    private String nome;
    private String ra;
    private String[] livrosEmprestados = new String[3];

    public Estudante(String curso, int periodo, String nome, String ra) {
        this.curso = curso;
        this.periodo = periodo;
        this.nome = nome;
        this.ra = ra;
    }

    public String getCurso() {
        return curso;
    }

    public int getPeriodo() {
        return periodo;
    }

    public String getNome() {
        return nome;
    }

    public String getRa() {
        return ra;
    }

    public boolean emprestarLivro(String codigoLivro) {
        for (int i = 0; i < livrosEmprestados.length; i++) {
            if (livrosEmprestados[i] == null) {
                livrosEmprestados[i] = codigoLivro;
                return true;
            }
        }
        return false;
    }

    public boolean devolverLivro(String codigoLivro) {
        for (int i = 0; i < livrosEmprestados.length; i++) {
            if (codigoLivro.equals(livrosEmprestados[i])) {
                livrosEmprestados[i] = null;
                return true;
            }
        }
        return false;
    }

    public int getQuantidadeLivros() {
        int count = 0;
        for (String codigo : livrosEmprestados) {
            if (codigo != null) {
                count++;
            }
        }
        return count;
    }

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(curso).append(";").append(periodo).append(";")
          .append(nome).append(";").append(ra).append(";");
        sb.append("Livros: ");
        for (String codigo : livrosEmprestados) {
            if (codigo != null) {
                sb.append(codigo).append(" ");
            }
        }
        return sb.toString().trim();
    }
}
