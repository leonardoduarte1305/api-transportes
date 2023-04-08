create table destino
(
    id        integer not null auto_increment,
    excluido  bit     not null,
    status    varchar(255),
    sede_id   bigint  not null,
    viagem_id integer,
    primary key (id)
) engine = InnoDB;


create table material
(
    id        integer not null auto_increment,
    descricao varchar(255),
    nome      varchar(255),
    primary key (id)
) engine = InnoDB;


create table material_quantidade_setor
(
    id          integer not null auto_increment,
    quantidade  integer,
    destino_id  integer,
    material_id integer not null,
    setor_id    integer not null,
    primary key (id)
) engine = InnoDB;


create table motorista
(
    id       integer not null auto_increment,
    carteira varchar(255),
    email    varchar(255),
    nome     varchar(255),
    primary key (id)
) engine = InnoDB;


create table sede
(
    id         bigint not null auto_increment,
    cep        varchar(255),
    cidade     varchar(255),
    nome       varchar(255),
    numero     integer,
    observacao varchar(255),
    rua        varchar(255),
    uf         ENUM ('RO','AC','AM','RR','PA','AP','TO','MA','PI','CE','RN','PB','PE','AL','SE','BA','MG','ES','RJ','SP','PR','SC','RS','MS','MT','GO','DF'),
    primary key (id)
) engine = InnoDB;


create table sede_inscritos
(
    sede_id          bigint not null,
    inscritos_emails varchar(255)
) engine = InnoDB;


create table setor
(
    id   integer not null auto_increment,
    nome varchar(255),
    primary key (id)
) engine = InnoDB;


create table veiculo
(
    id      integer not null auto_increment,
    ano     integer,
    marca   varchar(255),
    modelo  varchar(255),
    placa   varchar(255),
    renavan decimal(38, 2),
    tamanho varchar(255),
    primary key (id)
) engine = InnoDB;


create table viagem
(
    id             integer not null auto_increment,
    datetime_saida varchar(255),
    datetime_volta varchar(255),
    encerrado      bit     not null,
    excluido       bit     not null,
    status         varchar(255),
    motorista_id   integer not null,
    veiculo_id     integer not null,
    primary key (id)
) engine = InnoDB;


alter table destino
    add constraint FK_destino_sede
        foreign key (sede_id)
            references sede (id);


alter table destino
    add constraint FK_destino_viagem
        foreign key (viagem_id)
            references viagem (id);


alter table material_quantidade_setor
    add constraint FK_materialquantidadesetor_destino
        foreign key (destino_id)
            references destino (id);


alter table material_quantidade_setor
    add constraint FK_materialquantidadesetor_material
        foreign key (material_id)
            references material (id);


alter table material_quantidade_setor
    add constraint FK_materialquantidadesetor_setor
        foreign key (setor_id)
            references setor (id);


alter table sede_inscritos
    add constraint FK_sedeinscritos_sede
        foreign key (sede_id)
            references sede (id);


alter table viagem
    add constraint FK_viagem_motorista
        foreign key (motorista_id)
            references motorista (id);


alter table viagem
    add constraint FK_viagem_veiculo
        foreign key (veiculo_id)
            references veiculo (id);
