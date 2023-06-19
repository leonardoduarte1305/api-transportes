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

Você deve ir no grupo do Teams e baixar de lá o arquivo local.env e colocá-lo na [raiz](./) do projeto.

#### *Lembre-se de nunca commitar credenciais. As credenciais já commitadas são exemplos e apenas para desenvolvimento local da aplicação, elas nunca serão usadas em produção.

## 2- Levantando os serviços necessários

### Comece levantando o banco de dados Postgres executando na pasta [raiz](./) do projeto:

```bash
docker run -d \
-p 5432:5432 \
--name postgresql \
--env-file local.env \
postgres:alpine
```

<hr>

## 3 - Crie os databases necessários

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
docker run -d --rm \
-p 80:8080 \
--name keycloak \
-e KEYCLOAK_DATABASE_HOST=$(docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' $(docker container ls | grep postgresql | awk '{print $1}')) \
--env-file local.env \
bitnami/keycloak:21
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
export $(xargs < local.env) && ./mvnw clean install && cd ${PWD}/service && ./mvnw spring-boot:run
```

## 5 - Rodando a imagem de container Docker da Api-Transportes
### Com Autenticação e autorização

**** NÃO FUNCIONANDO ****
```bash
docker run -it --rm \
-p 8080:8080 \
--name api-transportes \
-e AUTH_SERVER_URL=http://$(docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' $(docker container ls | grep keycloak | awk '{print $1}')):80/auth \
-e TRUSTED_ISSUERS=http://$(docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' $(docker container ls | grep keycloak | awk '{print $1}')):80/realms/api-transportes \
-e JWK_SET_URI=http://$(docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' $(docker container ls | grep keycloak | awk '{print $1}')):80/realms/api-transportes/protocol/openid-connect/certs \
-e DB_HOST=$(docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' $(docker container ls | grep postgresql | awk '{print $1}')) \
--env-file backendlocal.env \
leonardoduarte1305/api-transportes-service:Auth-18-06
```


## 5 - Rodando a imagem de container Docker da Api-Transportes
### Sem Autenticação e autorização

```bash
docker run -d --rm \
-p 8080:8080 \
--name api-transportes \
-e DB_HOST=$(docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' $(docker container ls | grep postgresql | awk '{print $1}')) \
--env-file local.env \
leonardoduarte1305/api-transportes-service:noAuth-18-06
```

## 6 - Rodando a imagem de container Docker do Frontend

```bash
docker run -d --rm \
-p 4200:80 \
--name frontend \
leonardoduarte1305/stm-frontend:14-06
```
Acesse http://localhost:4200
