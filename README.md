# LogInsight: Pipeline ETL de logs em Java

Aplicação em Java que lê logs de servidor web (formato Apache/Nginx), extrai os dados com Regex, gera métricas úteis para suporte e grava tudo em um banco PostgreSQL.

## Por que este projeto

Logs de servidor são extensos e difíceis de analisar na mão. Este projeto automatiza esse trabalho: transforma texto bruto em dados estruturados, responde perguntas como "quais IPs mais geram erros?" e guarda o resultado num banco para consultas em SQL.

## Como funciona

```
access.log  ->  Extract  ->  Transform  ->  Load
                (leitura)    (Regex +       (PostgreSQL)
                             métricas)
```

- **Extract:** lê o arquivo `data/access.log` linha por linha.
- **Transform:** o `LogParser` extrai IP, data, método, rota e status com Regex e devolve um `LogEntry`. Linhas inválidas são descartadas sem quebrar o programa. Depois, o `Main` calcula o Top 3 de IPs com mais erros (status >= 400) e a rota mais acessada com sucesso (status 200).
- **Load:** o `LogRepository` grava cada `LogEntry` na tabela `tb_log_entries` via JDBC.

## Tecnologias

- Java 17+
- Maven
- Regex (`java.util.regex`)
- Collections (`List`, `Map`)
- PostgreSQL e JDBC
- Git e GitHub

## Estrutura do projeto

```
log-analyzer
├── data/access.log                  # log de exemplo (IPs fictícios)
├── sql/criar_tabela.sql             # script do banco e da tabela
├── config.properties.example        # modelo de configuração
├── src/main/java/com/alana
│   ├── Main.java                    # orquestra o fluxo e calcula as métricas
│   ├── LogEntry.java                # modelo de uma linha de log
│   ├── LogParser.java               # Regex: texto -> LogEntry
│   ├── Conexao.java                 # abre a conexão com o PostgreSQL
│   └── LogRepository.java           # INSERT no banco
└── pom.xml
```

## Como rodar

**Pré-requisitos:** Java 17+, Maven e PostgreSQL instalados.

1. Clone o repositório:
   ```
   git clone https://github.com/AlanaMarg/log-etl-pipeline.git
   ```
2. No pgAdmin, rode o script `sql/criar_tabela.sql` (crie o banco `loginsight`, conecte nele e crie a tabela).
3. Copie `config.properties.example` para `config.properties` e preencha com os seus dados do PostgreSQL.
4. Rode a classe `Main` pelo IntelliJ.

## Exemplo de saída

```
Connecting to database...

------RESUMO------
Quantidade de logs lidos: 32
Quantidade de logs validos: 30
Quantidade de logs invalidos: 2

-----TOP 3 IP's COM MAIS ERROS-----
1. 10.0.0.5 - 6 erros.
2. 172.16.0.8 - 5 erros.
3. 10.0.0.9 - 3 erros.

-----ROTA MAIS ACESSADA-----
/produtos - 6 acessos.

-----GRAVACAO NO BANCO-----
Gravados no banco: 30
```

## O que aprendi

## O que aprendi

Antes deste projeto eu só conhecia Java básico e banco de dados. Regex, JDBC e o
uso de Map eram novidade para mim, e aprendi fazendo, um passo de cada vez.

**Regex.** É uma forma de descrever o formato de um texto para extrair partes
dele. No projeto, usei para dividir cada linha do log em partes menores (IP,
data, método, rota e status) para poder analisá-las. A maior dificuldade foi a
sintaxe, que é muito específica e diferencia maiúsculas de minúsculas: por
exemplo, `\s` (espaço) e `\S` (qualquer coisa que não é espaço) têm
significados opostos, e um erro de uma letra fez o parser não encontrar nada.

**HashMap e lista.** Usei um HashMap para guardar cada IP e sua quantidade de
erros. Como ele não tem ordem nem posição, foi preciso copiar os pares para uma
lista, que permite ordenar e pegar o Top 3. Também errei a contagem algumas
vezes: coloquei o `+ 1` dentro do `get` (`get(e.getIp() + 1)`), então o Java
buscava uma chave que não existia.

**Conexão com o banco (JDBC).** Adicionei o driver do PostgreSQL ao projeto
para que o código Java consiga se comunicar com o banco. Gravei os dados com
`PreparedStatement`, usando `?` no lugar dos valores. Isso é uma medida de
segurança: como o log vem de fora, ele pode conter texto malicioso, e o
`PreparedStatement` trata o que entra pelo `?` como dado, e não como comando
SQL.

**Segurança.** Aprendi que a senha do banco não pode ir para o GitHub. Guardei
os dados de acesso no `config.properties` e usei o `.gitignore` para que esse
arquivo não seja enviado no push. Também aprendi a conferir a lista de arquivos
antes de cada commit.

## Próximos passos

- Converter o timestamp para `LocalDateTime`
- Guardar o tamanho da resposta (bytes)
- Evitar dados duplicados ao rodar o programa mais de uma vez