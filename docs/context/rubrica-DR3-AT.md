# Rúbrica — DR3-AT

> Transcrição da rúbrica fornecida pelo professor. **Esta é a fonte de verdade acadêmica** da
> entrega.

## 1. Desenvolver microserviços cloud nativos com Spring Boot e Spring Cloud

| # | Critério |
|:---:|---|
| 1.1 | O aluno criou o microsserviço fornecedores-service configurando corretamente o arquivo pom.xml com as dependências, pacotes e a porta 8084? |
| 1.2 | O aluno registrou o novo microsserviço no Eureka Server e comprovou sua exibição no painel de controle de serviços descobertos? |
| 1.3 | O aluno externalizou as configurações do serviço criando o arquivo no config-repo e demonstrou a aplicação consumindo os dados do Config Server? |
| 1.4 | O aluno implementou a comunicação entre o fornecedores-service e o produtos-service utilizando o cliente declarativo Spring Cloud OpenFeign? |

## 2. Publicar microserviços Spring Boot, orquestrando containers com Docker e Kubernetes

| # | Critério |
|:---:|---|
| 2.1 | O aluno elaborou o arquivo Dockerfile com as instruções e a porta adequadas para a conteinerização do microsserviço fornecedores-service? |
| 2.2 | O aluno criou o arquivo de configuração para o perfil Docker no repositório apontando o nome do serviço do Eureka na rede de containers? |
| 2.3 | O aluno adicionou e estruturou o bloco de inicialização do fornecedores-service dentro do arquivo docker-compose.yml do projeto? |
| 2.4 | O aluno subiu toda a infraestrutura orquestrada via Docker Compose com um comando unificado e comprovou o acesso integrado pelo gateway? |

## 3. Desenvolver microserviços usando Reactive Spring

| # | Critério |
|:---:|---|
| 3.1 | O aluno implementou a entidade Fornecedor e o repositório de dados garantindo a inicialização automática de cinco registros no H2 Database? |
| 3.2 | O aluno desenvolveu os endpoints GET para a listagem e busca por ID lidando corretamente com retornos de erro (status HTTP 404)? |
| 3.3 | O aluno construiu o endpoint POST responsável por persistir o JSON recebido e devolver o novo fornecedor salvo acompanhado do status HTTP 201? |
| 3.4 | O aluno acessou os endpoints do novo microsserviço de forma roteada pelo Spring Cloud Gateway na porta 8085 aproveitando seu roteamento dinâmico? |

## 4. Desenvolver em grupo utilizando repositórios Git através do GitHub

| # | Critério |
|:---:|---|
| 4.1 | O aluno realizou o fork do repositório original para sua conta pessoal e clonou os arquivos do projeto para o seu ambiente local de desenvolvimento? |
| 4.2 | O aluno criou e utilizou a branch de trabalho seguindo estritamente o padrão de nomenclatura (atividade-seunome) exigido pelo enunciado? |
| 4.3 | O aluno editou o arquivo readme.md com as informações de identificação (nome completo e matrícula) e executou o commit adequadamente? |
| 4.4 | O aluno realizou o push da nova branch para o repositório remoto e abriu um Pull Request para a main do seu próprio fork? |

## 5. Publicar de forma automática microsserviços Spring Boot usando GitHub Actions e Kubernetes

| # | Critério |
|:---:|---|
| 5.1 | O aluno estruturou corretamente o diretório .github/workflows e adicionou o arquivo YAML para configurar a pipeline de integração contínua (CI)? |
| 5.2 | O aluno configurou o gatilho (trigger) do GitHub Actions de forma a iniciar o pipeline automaticamente a cada evento de push no repositório? |
| 5.3 | O aluno definiu as etapas do workflow no Actions executando o checkout do código e a correta configuração do ambiente de desenvolvimento Java 17? |
| 5.4 | O aluno parametrizou a execução automatizada da compilação com Maven e entregou a evidência do pipeline concluído com sucesso (check verde)? |

## Observação de leitura

A rúbrica e o enunciado citam Java 17. Por decisão do autor, a entrega usa a versão estável mais
recente de cada tecnologia: **Java 25 (LTS)**, Spring Boot 4.1.1 e Spring Cloud 2025.1.3. O Java 25
é a LTS mais recente, depois da 17 e da 21. O workflow do critério 5.3 configura o JDK como o
critério pede, só que na versão 25.
