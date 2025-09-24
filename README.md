
# 📖 Trabalho Bimestral – POO I

Trabalho apresentado ao Professor Me. Bruno Luiz Schuster Rech, como requisito parcial para a composição da nota do primeiro bimestre do 4º período do Curso de Bacharelado em Engenharia de Software da Faculdade Uniguaçu.

São Miguel do Iguaçu – PR, 23 de setembro de 2025.
##

> **"O impossível sempre parece impossível até que seja feito — e nada é mais forte do que pessoas unidas por um mesmo propósito."**  
> — *Nelson Mandela*

## 🧑‍💻 Autores

**Kauan Antônio Neves Gomes**  [![GitHub de Kauan](https://img.shields.io/badge/GitHub-kauansw2-black?logo=github)](https://github.com/kauansw2)  
**Matheus de Almeida Boava**  [![GitHub de Boava](https://img.shields.io/badge/GitHub-maboava-black?logo=github)](https://github.com/maboava)  
**Nichole Maria Furtado**  [![GitHub de Nichole](https://img.shields.io/badge/GitHub-Nichole_Furtado-black?logo=github)](https://github.com/Nichole-Furtado)  
**Rafael Rodrigues Pichibinski** [![GitHub de Rafael](https://img.shields.io/badge/GitHub-1Deatth-black?logo=github)](https://github.com/1Deatth)  
**Tais Mayme Ferrari**  [![GitHub de Tais](https://img.shields.io/badge/GitHub-Tais1905-black?logo=github)](https://github.com/Tais1905)  

## 📌 Objetivo

Sistema simples de empréstimo de livros em Java, utilizando:

* Classes e Objetos
* Arrays para armazenamento
* Manipulação de Arquivos Texto para persistência

**Funcionalidades principais:**

* Cadastrar e listar livros
* Cadastrar estudantes
* Realizar empréstimos (máx. 3 livros por estudante)
* Realizar devoluções
* Gerar arquivo texto (livros.txt) com os dados dos livros
## 📘 Nosso Projeto

A **Biblioteca Uniguaçu** é um aplicativo desktop modularizado em *Java 17 + Swing* (módulo `TrabalhoFinal`) voltado à gestão de acervo, cadastro discente e controle de empréstimos.  

A solução combina persistência leve em arquivos texto (`livros.txt` / `estudantes.txt`) com uma interface tabulada que centraliza rotinas de **CRUD**, filtros e fluxos de **empréstimo/devolução**.

## 🏗️ Arquitetura e Ciclo de Vida

- **`BibliotecaApp`**: ponto de entrada. Configura o *Look & Feel* nativo, exibe a *splash screen* (`SplashScreenWindow`) com barra de progresso e, após a carga, instancia a camada de domínio e a UI. 

- **`Biblioteca`**: núcleo de negócios. Gerencia vetores fixos (`30` livros, `10` estudantes) com operações de criação, busca, atualização, remoção e regras de empréstimo. Também encapsula a serialização/desserialização para `.txt`, garantindo recuperação automática de dados e *seed* de registros padrão quando os arquivos estão vazios.  
- **`BibliotecaUI`**: `JFrame` principal, dividido em abas (**Livros, Estudantes, Empréstimos**). Cada aba combina filtros, tabelas (`DefaultTableModel`) e botões de ação (renderizados/editados por `ButtonRenderer` / `ButtonEditor`) para acionar os métodos expostos por `Biblioteca`. O fechamento da janela força `salvarDados()`.

---

📌 **Ciclo básico:**  
`BibliotecaApp` → carrega dados em `Biblioteca` → injeta a instância em `BibliotecaUI`.  

Todos os fluxos de UI passam pela mesma instância centralizada, garantindo consistência entre abas e evitando leituras diretas dos arquivos.

## 📑 Modelo de Domínio

- **`Livro`**: encapsula metadados (*código, título, autor, ano, editora*) e o estado de empréstimo (flag + RA associado).  
  - Métodos de fábrica (`fromFileString`) e persistência (`toFileString`) padronizam o formato `;`-delimitado, evitando divergências na gravação.  

- **`Estudante`**: guarda dados acadêmicos (*curso, período, nome, RA*) e um vetor de até três códigos de livros (`LIMITE_EMPRESTIMOS = 3`).  
  - Oferece utilitários para verificar posse de livros, contar empréstimos vigentes e produzir a string de armazenamento.  

- **`Biblioteca`**: além das operações CRUD, aplica regras de negócio:  
  - ✅ Verificação de unicidade de código de livro / RA de estudante;  
  - ✅ Bloqueio de exclusão para livros emprestados ou estudantes com pendências;  
  - ✅ Controle de limite simultâneo de livros por estudante (retorna mensagens de status como *“Estudante já possui 3 livros”*).  

## 🖥️ Fluxos Técnicos na UI

1. **Aba Livros**  
   - Permite busca textual (*código, título, autor, editora ou RA do empréstimo*).  
   - Cadastro/edição via `JOptionPane` com validações:  
     - Campos obrigatórios.  
     - Ano deve ser numérico.  
     - Bloqueio de `;` nos campos.  
   - Remoção condicionada ao status **“Disponível”**.  

2. **Aba Estudantes**  
   - Filtro por *nome, RA ou curso*.  
   - Exibe resumo dos códigos emprestados.  
   - Formulários para criação/edição com validações:  
     - Período > 0.  
     - Sem `;` nos campos.  
   - Remoção apenas se não houver empréstimos ativos.  

3. **Aba Empréstimos**  
   - Tabela consolidada dos livros emprestados.  
   - Ação de devolução sincroniza `Livro` e `Estudante`.  
   - Painel inferior mostra resumo de limite: *“Aluno X possui n de 3 livros”*.  
   - Botão **“Emprestar livro”** só é habilitado após selecionar um aluno.  

## 💾 Persistência e Integridade

- Arquivos texto seguem o padrão:  
  - `;` → separador de campos.  
  - `,` → lista de livros por estudante.  
  - Métodos dedicados (`carregarLivros()`, `carregarEstudantes()`, `salvarLivros()`, `salvarEstudantes()`) centralizam leitura/gravação e tratam `IOExceptions`.  

- **`Biblioteca`** expõe somente **cópias dos arrays** (`getLivros()`, `getEstudantes()`), impedindo que a UI manipule diretamente os vetores internos.  

- **Mensagens de retorno** e **`JOptionPane`** garantem clareza em cada etapa crítica:  
  - Confirmação de exclusão.  
  - Aviso de limite atingido.  
  - Erros de validação.  

---

✅ Com essa estrutura, a aplicação mantém **baixo acoplamento** entre UI e domínio, garante **consistência dos dados** em arquivos simples e entrega uma experiência completa de gerenciamento para ambientes acadêmicos ou laboratoriais.

## 📚 Referências

- [Documentação Oficial do Java](https://docs.oracle.com/javase/8/docs/) — Guia completo da linguagem, APIs e boas práticas.  
- [Java Platform, Standard Edition 8 API Specification](https://docs.oracle.com/javase/8/docs/api/) — Referência detalhada de todas as classes e pacotes do Java SE.  
- [Java Swing Tutorial - Oracle](https://docs.oracle.com/javase/tutorial/uiswing/) — Documentação oficial para desenvolvimento de interfaces gráficas com Swing.  
- [Java Swing Documentation (Java SE 8)](https://docs.oracle.com/javase/8/docs/api/javax/swing/package-summary.html) — Referência das classes Swing.  
- [Java Object-Oriented Programming Concepts](https://docs.oracle.com/javase/tutorial/java/concepts/) — Conceitos fundamentais de Programação Orientada a Objetos em Java.  
- [Java Event Handling - Oracle Tutorial](https://docs.oracle.com/javase/tutorial/uiswing/events/index.html) — Guia para implementação de eventos em interfaces Swing.  
- [Java Naming Conventions](https://www.oracle.com/java/technologies/javase/codeconventions-namingconventions.html) — Convenções oficiais de nomenclatura em Java.  
- [Java Collections Framework](https://docs.oracle.com/javase/8/docs/technotes/guides/collections/overview.html) — Estruturas de dados fundamentais para projetos orientados a objetos.  
- [GitHub - Awesome Java](https://github.com/akullpp/awesome-java) — Lista curada com recursos, bibliotecas e frameworks da comunidade Java.  
- [Baeldung Java Tutorials](https://www.baeldung.com/java-tutorial) — Tutoriais modernos e práticos sobre Java, incluindo Swing e OOP.  
- [GeeksforGeeks - Java Swing](https://www.geeksforgeeks.org/java-swing/) — Exemplos práticos para construir GUIs com Swing.  
