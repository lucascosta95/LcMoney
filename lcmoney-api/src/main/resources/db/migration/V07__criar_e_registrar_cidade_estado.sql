CREATE TABLE estado (
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO estado (id, nome) VALUES(1, 'Acre');
INSERT INTO estado (id, nome) VALUES(2, 'Alagoas');
INSERT INTO estado (id, nome) VALUES(3, 'Amazonas');
INSERT INTO estado (id, nome) VALUES(4, 'Amapá');
INSERT INTO estado (id, nome) VALUES(5, 'Bahia');
INSERT INTO estado (id, nome) VALUES(6, 'Ceará');
INSERT INTO estado (id, nome) VALUES(7, 'Distrito Federal');
INSERT INTO estado (id, nome) VALUES(8, 'Espírito Santo');
INSERT INTO estado (id, nome) VALUES(9, 'Goiás');
INSERT INTO estado (id, nome) VALUES(10, 'Maranhão');
INSERT INTO estado (id, nome) VALUES(11, 'Minas Gerais');
INSERT INTO estado (id, nome) VALUES(12, 'Mato Grosso do Sul');
INSERT INTO estado (id, nome) VALUES(13, 'Mato Grosso');
INSERT INTO estado (id, nome) VALUES(14, 'Pará');
INSERT INTO estado (id, nome) VALUES(15, 'Paraíba');
INSERT INTO estado (id, nome) VALUES(16, 'Pernambuco');
INSERT INTO estado (id, nome) VALUES(17, 'Piauí');
INSERT INTO estado (id, nome) VALUES(18, 'Paraná');
INSERT INTO estado (id, nome) VALUES(19, 'Rio de Janeiro');
INSERT INTO estado (id, nome) VALUES(20, 'Rio Grande do Norte');
INSERT INTO estado (id, nome) VALUES(21, 'Rondônia');
INSERT INTO estado (id, nome) VALUES(22, 'Roraima');
INSERT INTO estado (id, nome) VALUES(23, 'Rio Grande do Sul');
INSERT INTO estado (id, nome) VALUES(24, 'Santa Catarina');
INSERT INTO estado (id, nome) VALUES(25, 'Sergipe');
INSERT INTO estado (id, nome) VALUES(26, 'São Paulo');
INSERT INTO estado (id, nome) VALUES(27, 'Tocantins');



CREATE TABLE cidade (
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(50) NOT NULL,
  id_estado BIGINT(20) NOT NULL,
  FOREIGN KEY (id_estado) REFERENCES estado(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO cidade (id, nome, id_estado) VALUES (1, 'Belo Horizonte', 11);
INSERT INTO cidade (id, nome, id_estado) VALUES (2, 'Uberlândia', 11);
INSERT INTO cidade (id, nome, id_estado) VALUES (3, 'Uberaba', 11);
INSERT INTO cidade (id, nome, id_estado) VALUES (4, 'São Paulo', 26);
INSERT INTO cidade (id, nome, id_estado) VALUES (5, 'Campinas', 26);
INSERT INTO cidade (id, nome, id_estado) VALUES (6, 'Rio de Janeiro', 19);
INSERT INTO cidade (id, nome, id_estado) VALUES (7, 'Angra dos Reis', 19);
INSERT INTO cidade (id, nome, id_estado) VALUES (8, 'Goiânia', 9);
INSERT INTO cidade (id, nome, id_estado) VALUES (9, 'Caldas Novas', 9);



ALTER TABLE pessoa DROP COLUMN cidade;
ALTER TABLE pessoa DROP COLUMN estado;
ALTER TABLE pessoa ADD COLUMN id_cidade BIGINT(20);
ALTER TABLE pessoa ADD CONSTRAINT fk_pessoa_cidade FOREIGN KEY (id_cidade) REFERENCES cidade(id);

UPDATE pessoa SET id_cidade = 2;