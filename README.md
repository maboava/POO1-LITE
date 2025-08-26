# 📖 Trabalho Bimestral – Programação Orientada a Objetos

## 📌 Objetivo
Sistema simples de **empréstimo de livros** em Java, utilizando:
- **Classes e Objetos**
- **Arrays** para armazenamento
- **Manipulação de Arquivos Texto** para persistência  

Funcionalidades principais:
- Cadastrar e listar livros  
- Cadastrar estudantes  
- Realizar empréstimos (máx. 3 livros por estudante)  
- Realizar devoluções  
- Gerar arquivo texto (`livros.txt`) com os dados dos livros  

---

## 🗂 Estrutura do Projeto

src/
└── trabalhofinal/
├── model/
│ ├── Livro.java
│ └── Estudante.java
│
├── service/
│ └── BibliotecaService.java
│
├── io/
│ └── LivroFileRepository.java
│
└── view/
└── Main.java // Menu de interação

markdown
Copiar código

---

## 🧩 Classes

### 🔹 `Livro`
- `codigo : int`
- `titulo : String`
- `autor : String`
- `ano : int`
- `editora : String`
- `emprestado : boolean`

### 🔹 `Estudante`
- `curso : String`
- `periodo : int`
- `nome : String`
- `ra : String`
- `codigosEmprestados : int[3]`
- `qtdEmprestados : int`

### 🔹 `BibliotecaService`
- `adicionarLivro(Livro l)`
- `listarLivros(boolean somenteDisponiveis)`
- `adicionarEstudante(Estudante e)`
- `emprestar(String ra, int codigoLivro)`
- `devolver(String ra, int codigoLivro)`

### 🔹 `LivroFileRepository`
- `salvar(Livro[] livros, int qtd, String caminho)`
- *(opcional)* `carregar(Livro[] livros, String caminho)`

### 🔹 `Main`
- Exibe o **menu de console**
- Recebe entradas do usuário via `Scanner`
- Encaminha chamadas para o `BibliotecaService`

---

## 📜 Menu Principal
====== Biblioteca ======
1 - Cadastrar Livro
2 - Listar Livros
3 - Cadastrar Estudante
4 - Emprestar Livro
5 - Devolver Livro
6 - Salvar Arquivo de Livros
0 - Sair
markdown
Copiar código

---

## 🚀 Passo a Passo de Desenvolvimento

1. **Criar projeto no Eclipse/IntelliJ**  
   - Adicionar pacotes `model`, `service`, `io`, `view`.

2. **Implementar classes `Livro` e `Estudante`**  
   - Construtores, getters/setters, `toString()`.

3. **Implementar `BibliotecaService`**  
   - Arrays fixos (`Livro[200]`, `Estudante[100]`)  
   - Contadores (`qtdLivros`, `qtdEstudantes`)  
   - Regras de validação (livro já emprestado, estudante com 3 livros, etc.)

4. **Criar `LivroFileRepository`**  
   - Método `salvar` gera `livros.txt` no formato:  
     ```
     codigo|titulo|autor|ano|editora|emprestado
     ```

5. **Montar o menu em `Main`**  
   - `Scanner` para entrada  
   - `switch-case` para opções do usuário  

6. **Testar todos os cenários**  
   - Emprestar livro inexistente → erro  
   - Estudante com 3 livros → erro  
   - Devolver sem estar emprestado → erro  
   - Gerar arquivo `livros.txt` com sucesso  

---

## 📂 Saídas Esperadas

- **Execução em console** mostrando menus e mensagens de sucesso/erro.  
- **Arquivo `livros.txt`** gerado no diretório do projeto contendo os dados dos livros.  

Exemplo de linha:
101|Estruturas de Dados|N. Wirth|2010|Campus|false

yaml
Copiar código

---

## ✅ Entregáveis
- Código-fonte organizado em pacotes  
- Arquivo `livros.txt` gerado pelo sistema  
- Demonstração do funcionamento (apresentação em sala)  