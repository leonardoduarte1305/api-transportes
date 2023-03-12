Como rodar o projeto:

Na primeira vez cada comando deve ser executado separadamente:

## MySql

Levante o container do Mysql usando:

```bash
docker-compose up -d mysql
```

<hr>

## PostgreSql

Levante o container do Postgres usando:

```bash
docker-compose up -d postgres
```

Conecte-se no container do PostgresSQL usando o comando:

```bash
docker exec -it $(docker container ls | grep postgres | awk '{print $1}') /bin/bash
```

Conecte-se no servidor do Postgres digitando:

```bash
psql postgres postgres
```

Crie o database usado pelo Keycloak:

```bash
create database bitnami_keycloak
```

Para verificar a criação do database use:

```bash
\list
```

<hr>

## Keycloak

No terminal use o comando:

```bash
docker-compose -d keycloak
```

### Mais informações sobre o Keycloak serão adicionadas futuramente
