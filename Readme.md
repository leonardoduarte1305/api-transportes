### Desenvolvedores:

- [Danilo de Jesus Santos](https://www.linkedin.com/in/danilo-jesus-santos/) - danilojesus4847@gmail.com
- [Gabriel Eduardo dos Santos Garbin](https://www.linkedin.com/in/gabriel-eduardo-sg/) - gabriel.garbin09@gmail.com
- [Gustavo Cardoso da Costa](https://www.linkedin.com/in/gustavo-cardosodacosta/) - gustavocardosodacosta2@gmail.com
- [Leonardo Gonçalves Duarte da Silva](https://www.linkedin.com/in/leonardoduarte1305/) - leonardoduarte1305@gmail.com

<hr>

# Api-Transportes - Backend

## Overview

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

- Você deve criar um arquivo chamado local.env e colocá-lo na [raiz](./) do projeto. As variáveis de ambiente
  necessárias se encontram no final deste arquivo de Readme.

## 2- Levantando os serviços necessários

### Comece levantando o banco de dados Postgres executando na pasta [raiz](./) do projeto:

Use este comando uma vez apenas.

```bash
docker run -d \
-p 5432:5432 \
--name postgresql \
--env-file local.env \
postgres:alpine
```

Para iniciar novamente o postgres utilize:

```bash
docker start postgresql
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

### Sem Autenticação e autorização

```bash
docker run -d --rm \
-p 8080:8080 \
--name api-transportes \
-e DB_HOST=$(docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' $(docker container ls | grep postgresql | awk '{print $1}')) \
--env-file local.env \
leonardoduarte1305/api-transportes-service:noAuth-01-07
```

## 6 - Rodando a imagem de container Docker do Frontend

```bash
docker run -d --rm \
-p 4200:80 \
--name frontend \
leonardoduarte1305/stm-frontend:21-06
```

Acesse http://localhost:4200

- O repositório do frontend desenvolvido em Angular pode ser encontrado
  em: [stm.senac](https://github.com/Danilo4847/stm.senac)

Variáveis de ambiente necessárias para toda a plataforma STM, você precisa modificar os valores de acordo com o seu
cenário:

```
POSTGRES_USER=postgres
POSTGRES_PASSWORD=senhaSegura
DB_DATABASE=apitransportes
DB_HOST=localhost
DB_PORT=5432
DB_VENDOR=postgresql

SPRING_MAIL_HOST=smtp.gmail.com
SPRING_MAIL_PORT=587
SPRING_MAIL_USERNAME=email-exemplo@gmail.com
SPRING_MAIL_PASSWORD=umaSenhaMuitoSegura

ACTIVE_PROFILE=production
SERVER_PORT=8080

CLIENT_NAME=nosso-keycloak-client
REALM_NAME=master
TRUSTED_ISSUERS=http://localhost/realms/master

KEYCLOAK_ADMIN_USER=admin
KEYCLOAK_ADMIN_PASSWORD=admin
KEYCLOAK_MANAGEMENT_USER=manager
KEYCLOAK_MANAGEMENT_PASSWORD=manager

KEYCLOAK_DATABASE_USER=postgres
KEYCLOAK_DATABASE_PASSWORD=senhaSegura
KEYCLOAK_DATABASE_NAME=bitnami_keycloak
KEYCLOAK_DATABASE_HOST=localhost
KEYCLOAK_DATABASE_PORT=5432
KEYCLOAK_DATABASE_VENDOR=postgresql

IP_MAQUINA_HOST=192.168.0.50
```

O IP_MAQUINA_HOST é o IP do seu computador na sua rede local. No Linux você pode conseguí-lo como resultado do comando:
```bash
ifconfig
```
no exemplo a seguir está logo após a palavra *inet*:

```
wlo1: flags=4163<UP,BROADCAST,RUNNING,MULTICAST>  mtu 1500
  inet 192.168.0.50  netmask 255.255.255.0  broadcast 192.168.0.255
  inet6 fe80::7e95:7791:1fba:5897  prefixlen 64  scopeid 0x20<link>
  inet6 2804:14d:baa0:98e2:76ae:fda8:329:d723  prefixlen 128  scopeid 0x0<global>
  ether ec:63:d7:5b:ae:e6  txqueuelen 1000  (Ethernet)
  RX packets 288712  bytes 183103075 (183.1 MB)
  RX errors 0  dropped 0  overruns 0  frame 0
  TX packets 432888  bytes 140848792 (140.8 MB)
  TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0
```
Se você usar:
```bash
echo "$(ip addr show wlo1 | grep "inet " | awk '{print $2}' | cut -d/ -f1)"
```
o resultado deve ser algo parecido com:
```
192.168.0.50
```

Qualquer dúvida entre em contato.

## Se quiser checar outros recursos da aplicação:
Para usar o Prometheus e o Grafana algumas configurações a mais são necessárias.

### Levante o container do Prometheus usando:
```bash
docker run -d --rm --name prometheus -v $(pwd)/prometheus.yml:/etc/prometheus/prometheus.yml -p 9090:9090 prom/prometheus:latest
```

### Levante o container do Grafana usando:

```bash
docker run -d --rm --name grafana -v $(pwd)/Grafana:/var/lib/grafana -p 3000:3000 grafana/grafana
```

Use: admin como login e senha e na tela seguinte crie um usuário ou apelas clique em skip
