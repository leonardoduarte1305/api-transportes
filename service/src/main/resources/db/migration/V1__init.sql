create table destino
(
    id        serial  not null,
    excluido  boolean not null,
    status    varchar(255),
    sede_id   bigint  not null,
    viagem_id integer,
    primary key (id)
);

create table material
(
    id        serial not null,
    descricao varchar(255),
    nome      varchar(255),
    primary key (id)
);

create table material_quantidade_setor
(
    id          serial  not null,
    quantidade  integer,
    destino_id  integer,
    material_id integer not null,
    setor_id    integer not null,
    primary key (id)
);

create table motorista
(
    id       serial not null,
    carteira varchar(255),
    email    varchar(255),
    nome     varchar(255),
    primary key (id)
);

create table sede
(
    id         bigserial not null,
    cep        varchar(255),
    cidade     varchar(255),
    nome       varchar(255),
    numero     integer,
    observacao varchar(255),
    rua        varchar(255),
    uf         varchar(255),
    primary key (id)
);

create table sede_inscritos
(
    sede_id          bigint not null,
    inscritos_emails varchar(255)
);

create table setor
(
    id   serial not null,
    nome varchar(255),
    primary key (id)
);

create table veiculo
(
    id      serial not null,
    ano     integer,
    marca   varchar(255),
    modelo  varchar(255),
    placa   varchar(255),
    renavan numeric(38, 2),
    tamanho varchar(255),
    primary key (id)
);

create table viagem
(
    id             serial  not null,
    datetime_saida varchar(255),
    datetime_volta varchar(255),
    encerrado      boolean not null,
    excluido       boolean not null,
    id_sede        integer,
    status         varchar(255),
    motorista_id   integer not null,
    veiculo_id     integer not null,
    primary key (id)
);

alter table destino
    add constraint FK_destino_sede
        foreign key (sede_id)
            references sede;


alter table destino
    add constraint FK_destino_viagem
        foreign key (viagem_id)
            references viagem;


alter table material_quantidade_setor
    add constraint FK_materialquantidadesetor_destino
        foreign key (destino_id)
            references destino;


alter table material_quantidade_setor
    add constraint FK_materialquantidadesetor_material
        foreign key (material_id)
            references material;


alter table material_quantidade_setor
    add constraint FK_materialquantidadesetor_setor
        foreign key (setor_id)
            references setor;


alter table sede_inscritos
    add constraint FK_sedeinscritos_sede
        foreign key (sede_id)
            references sede;


alter table viagem
    add constraint FK_viagem_motorista
        foreign key (motorista_id)
            references motorista;


alter table viagem
    add constraint FK_viagem_veiculo
        foreign key (veiculo_id)
            references veiculo;
