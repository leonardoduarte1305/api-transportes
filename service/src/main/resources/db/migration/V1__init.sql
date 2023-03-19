CREATE TABLE IF NOT EXISTS material (
id INT NOT NULL AUTO_INCREMENT,
descricao VARCHAR(255) NULL DEFAULT NULL,
nome VARCHAR(255) NULL DEFAULT NULL,
PRIMARY KEY (id))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS motorista (
id INT NOT NULL AUTO_INCREMENT,
carteira VARCHAR(255) NULL DEFAULT NULL,
email VARCHAR(255) NULL DEFAULT NULL,
nome VARCHAR(255) NULL DEFAULT NULL,
PRIMARY KEY (id))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS veiculo (
id INT NOT NULL AUTO_INCREMENT,
ano INT NULL DEFAULT NULL,
marca VARCHAR(255) NULL DEFAULT NULL,
modelo VARCHAR(255) NULL DEFAULT NULL,
placa VARCHAR(255) NULL DEFAULT NULL,
renavan DECIMAL(38,2) NULL DEFAULT NULL,
tamanho VARCHAR(255) NULL DEFAULT NULL,
PRIMARY KEY (id))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS sede (
id BIGINT NOT NULL AUTO_INCREMENT,
cep VARCHAR (255) NULL DEFAULT NULL,
cidade VARCHAR (255) NULL DEFAULT NULL,
nome VARCHAR (255) NULL DEFAULT NULL,
numero INT NULL DEFAULT NULL,
observacao VARCHAR (255) NULL DEFAULT NULL,
rua VARCHAR (255) NULL DEFAULT NULL,
uf ENUM("RO","AC","AM","RR","PA","AP","TO",
"MA","PI","CE","RN","PB","PE","AL",
"SE","BA","MG","ES","RJ","SP","PR",
"SC","RS","MS","MT","GO","DF") NULL DEFAULT NULL,
PRIMARY KEY (id))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS destino (
id INT NOT NULL AUTO_INCREMENT,
sede_id INT NULL DEFAULT NULL,
status VARCHAR(255) NULL DEFAULT NULL,
PRIMARY KEY (id))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS material_quantidade_setor (
id INT NOT NULL AUTO_INCREMENT,
material_id INT NULL DEFAULT NULL,
quantidade INT NULL DEFAULT NULL,
setor_destino SMALLINT NULL DEFAULT NULL,
PRIMARY KEY (id))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS destino_materiais_qntd_setor (
destino_id INT NOT NULL,
materiais_qntd_setor_id INT NOT NULL,
UNIQUE INDEX UK_nu611h7nc6hqw7qb5ueb4b449 (materiais_qntd_setor_id ASC) VISIBLE,
INDEX FKik9teof2k3lhteejemiyc3y8y (destino_id ASC) VISIBLE,
CONSTRAINT FKd6knskcd7gdxnujwtp5a4ejc4
FOREIGN KEY (materiais_qntd_setor_id)
REFERENCES material_quantidade_setor (id),
CONSTRAINT FKik9teof2k3lhteejemiyc3y8y
FOREIGN KEY (destino_id)
REFERENCES destino (id))
ENGINE = InnoDB;
