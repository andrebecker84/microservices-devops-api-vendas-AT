# Enunciado — DR3-AT

> Transcrição do enunciado fornecido pelo professor. **Esta é a fonte de verdade acadêmica** da
> entrega.

## Atividade — Microsserviços com Spring Cloud

**Base:** o projeto `spring-microservices-exercicio`, feito em sala.

**Entrega:**

- link do repositório no GitHub;
- PDF com os prints pedidos.

## Leia antes de começar

Não crie um projeto do zero. Use o mesmo projeto que fizemos em sala. Você vai criar um novo
microsserviço, o `fornecedores-service`, e ligá-lo ao que já existe. A forma mais rápida é copiar a
pasta `clientes-service` inteira e trocar os nomes, porque ela já vem com entidade, repositório, H2,
Eureka e Config Server funcionando. Em quase toda questão a tarefa é copiar de um serviço que já
está pronto e trocar cliente por fornecedor. Se travar, abra o serviço equivalente no projeto e use
como gabarito.

Você precisa ter instalado Java 17, Maven, Docker e ter uma conta no GitHub. As portas 8761, 8888,
8081, 8082, 8083 e 8085 já estão ocupadas pelo projeto, então o seu serviço vai usar a porta 8084.

## Exercício 1

- Faça um fork do repositório do projeto para a sua conta do GitHub, clone o seu fork na sua
  máquina e suba o `eureka-server` e depois o `produtos-service`.
- Para testar, abra o endereço `localhost:8761` e veja o `produtos-service` na lista de serviços
  registrados.
- Entregue um print do painel do Eureka com o serviço registrado.

## Exercício 2

- Crie uma branch chamada `atividade-seunome`.
- Edite o `readme.md` acrescentando uma linha com o seu nome completo e a sua matrícula.
- Faça o commit, envie a branch para o GitHub e abra um Pull Request dessa branch para a `main` do
  seu próprio fork.
- A partir daqui, todos os commits da atividade vão nessa mesma branch.
- Entregue um print da tela do Pull Request aberto.

## Exercício 3

- Crie o novo microsserviço a partir de uma cópia do `clientes-service`, ajustando o `artifactId` e o
  `name` no `pom.xml`, o nome do pacote, o nome da classe principal, o `spring.application.name` e a
  porta, que deve ser a 8084.
  - Para testar, a aplicação precisa subir sem erro e ocupar a porta 8084.
- Entregue um print do terminal mostrando a aplicação iniciada.

## Exercício 4

- Seguindo o modelo da entidade `Cliente`, crie a entidade `Fornecedor` com id gerado
  automaticamente, nome obrigatório e CNPJ obrigatório e único.
- Crie também o repositório correspondente e uma classe que cadastre cinco fornecedores
  automaticamente quando a aplicação subir.
- Para testar, consulte a tabela pelo console do H2.
- Entregue um print do H2 Console com os cinco registros.

## Exercício 5

- Crie a camada de serviço e o controller com dois endpoints:
  - O `GET /fornecedores` lista todos os fornecedores.
  - O `GET /fornecedores/{id}` devolve o fornecedor, ou o status 404 quando o id não existir.
  - O controller do `produtos-service` já resolve exatamente esse caso do 404, use como
    referência.
- Entregue um print das duas respostas, e a segunda precisa mostrar o código 404.

## Exercício 6

- Faça o seu serviço se registrar no Eureka, como os outros já fazem.
- Confira a dependência no `pom.xml` e as propriedades necessárias.
- Para testar, com o Eureka no ar, suba o serviço e recarregue o endereço `localhost:8761`.
- Entregue um print do Eureka com o `FORNECEDORES-SERVICE` na lista.

## Exercício 7

- Crie o arquivo de configuração do seu serviço dentro da pasta `config-repo`, com a porta, o banco
  H2 e o Eureka.
- Deixe no `application.properties` do serviço apenas o nome da aplicação e o endereço do
  config-server.
- Suba o config-server antes do seu serviço.
- Para testar, peça a configuração direto ao config-server pelo navegador e veja as suas
  propriedades.
- Entregue um print dessa resposta e um print do serviço subindo com a porta vinda do Config
  Server.

## Exercício 8

- Com o eureka, o config-server, o gateway e o seu serviço no ar, acesse a lista de fornecedores
  passando pelo gateway, na porta 8085, e não mais pela porta 8084.
  - O gateway descobre os serviços pelo nome registrado no Eureka, então você não precisa escrever
    nenhuma rota.
- Entregue um print da resposta vinda da porta 8085.

## Exercício 9

- Acrescente o endpoint `POST /fornecedores`, que recebe um fornecedor em JSON, salva no banco e
  devolve o status 201 junto com o objeto criado.
- Para testar, envie um novo fornecedor e confirme que ele aparece no `GET /fornecedores`.
- Entregue um print mostrando o 201 e o id do novo registro.

## Exercício 10

- Faça o seu serviço consultar o `produtos-service` usando Feign.
- Adicione a dependência do OpenFeign, habilite os clientes Feign na aplicação, crie a interface do
  cliente e o DTO do produto, e crie o endpoint `GET /fornecedores/produtos`, que devolve a lista de
  produtos obtida do outro serviço.
- O `vendas-service` já faz uma chamada Feign ao `produtos-service`, é o mesmo caminho mudando só o
  método chamado.
- Para testar, com o eureka, o `produtos-service` e o seu serviço no ar, chame o novo endpoint.
- Entregue um print dos produtos aparecendo na resposta do seu serviço.

## Exercício 11

- Crie o `Dockerfile` do seu serviço, no mesmo modelo dos outros, ajustando a porta.
- Crie o arquivo de configuração do perfil docker na pasta `config-repo`, apontando o Eureka para o
  nome do serviço na rede do Docker.
- Acrescente o seu serviço ao `docker-compose.yml`, seguindo o bloco de um serviço que já existe.
- Para testar, suba tudo com um único `docker compose up --build` e acesse os fornecedores pelo
  gateway.
- Entregue um print dos containers rodando e um print da resposta pelo gateway.

## Exercício 12

- Crie um workflow do GitHub Actions no seu repositório, dentro da pasta `.github/workflows`, que a
  cada push baixe o código do repositório, instale o Java 17 e compile o seu `fornecedores-service`
  com o Maven.
- Faça o commit e o push do arquivo, que o GitHub executa o pipeline sozinho. Para testar, abra a aba
  Actions do seu repositório e acompanhe a execução.
- Entregue um print do pipeline concluído com o check verde.
