# Api-Transportes

<hr>

# Overview

### O projeto está separado em dois módulos:

### api-spec:

#### Contém a OpenAPI Specification para todos os recursos disponíveis do projeto.

### service:

#### Contém a implementação e todas as configurações para rodar o projeto.

<hr>

## Rodando o projeto localmente

## 1 - Requisitos mínimos:

#### Java 17 instalado

#### Docker instalado

#### Sistema Linux

#### Sistema Windows (utilizando o bash environment)*

### Informações Úteis

Na raiz do projeto você encontra o arquivo [compose.env](./compose.env) e dentro dele, na linha 15 a seguinte
expressão: `SPRING_MAIL_PASSWORD=` você deve ir no grupo do Teams e copiar de lá o token para o envio de email. Por
motivos de segurança ele não é fornecido junto do projeto.

#### *Lembre-se de nunca commitar credenciais. As credenciais já commitadas são exemplos e apenas para desenvolvimento local da aplicação, elas nunca serão usadas em produção.

## 2- Levantando os serviços necessários

### Levante o Postgres primeiramente executando na pasta [raiz](./) do projeto:

```bash
docker-compose up -d postgresql
```

<hr>

## 3 - Criando database no PostgreSQL

Conecte-se no container do PostgresSQL usando o comando:

```bash
docker exec -it $(docker container ls | grep postgresql | awk '{print $1}') /bin/bash
```

Conecte-se no servidor do Postgres digitando:

```bash
psql postgres postgres
```

Crie o database usado pela nossa Aplicação:

```bash
CREATE DATABASE apitransportes;
```

Crie o database usado pelo Keycloak:

```bash
CREATE DATABASE bitnami_keycloak;
```

Para verificar a criação dos databases use:

```bash
\list
```

<hr>

## 4 - Keycloak

### Levante o Keycloak executando na pasta [raiz](./) do projeto:

```bash
docker-compose up -d keycloak
```

Acesse http://localhost:80/admin/

    Longin: admin
    Senha: admin

Na Primeira Utilização é necessário criar:

    Realm:  Clique no dropdown à esquerda e em Create Realm
            Preencha o Realm name: api-transportes
            Clique em Create

    Client: Clique em Clients e em Create Client
            Preencha o Client ID: api-transportes-client
            Crie uma descrição se desejar
            Clique em Next e depois em Save

    Roles:  Na página final do passo anterior clique em Roles
            Clique em Create Roles
            Preencha a Role name: ADMIN ou USER
            Crie uma descrição se desejar
            Clique em Save
    
    Usuário:Clique em Users e em Create new user
            Preencha o username e todos os dados que desejar neste passo
            Clique em Create e depois em Role mapping
            Clique em Assign role e depois no dropdown à esquerda
            Selecione no dropdown: Filter by clients
            Marque a role que você anteriormente: ADMIN ou USER
            O nome do client que você cadastrou aparece à esquerda do nome da Role
            Clique em Assign

<hr>

## 5 - Rodando o projeto Api-Transportes

### Execute na pasta [raiz](./) o seguinte comando:

```bash
export $(xargs < .env) && ./mvnw clean install && cd ${PWD}/service && ./mvnw spring-boot:run
```

#### O arquivo .env não está junto do projeto, você deve baixá-lo no grupo do Teams

## 5 - Rodando a imagem de container Docker da Api-Transportes

```bash
docker-compose up -d api-transportes
```

#### O arquivo compose.env está junto do projeto, porém você deve pegar no grupo do Teams 
