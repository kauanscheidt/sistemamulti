CREATE DATABASE IF NOT EXISTS sistemamulti CHARACTER SET utf8mb4;
USE sistemamulti;

CREATE TABLE organizacao (
    codigo INT AUTO_INCREMENT PRIMARY KEY,
    nome_fantasia VARCHAR(100) NOT NULL UNIQUE,
    restricoes VARCHAR(255) NOT NULL DEFAULT ''
);

CREATE TABLE operador (
    codigo INT AUTO_INCREMENT PRIMARY KEY,
    nome_acesso VARCHAR(50) NOT NULL UNIQUE,
    credencial CHAR(64) NOT NULL,
    perfil ENUM('COMUM', 'ADMIN') NOT NULL
);

CREATE TABLE operador_organizacao (
    operador_codigo INT NOT NULL,
    organizacao_codigo INT NOT NULL,
    PRIMARY KEY (operador_codigo, organizacao_codigo),
    FOREIGN KEY (operador_codigo) REFERENCES operador(codigo),
    FOREIGN KEY (organizacao_codigo) REFERENCES organizacao(codigo)
);

CREATE TABLE publicacao (
    codigo INT AUTO_INCREMENT PRIMARY KEY,
    organizacao_codigo INT NOT NULL,
    titulo VARCHAR(150) NOT NULL,
    conteudo TEXT NOT NULL,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (organizacao_codigo) REFERENCES organizacao(codigo)
);

CREATE TABLE lancamento (
    codigo INT AUTO_INCREMENT PRIMARY KEY,
    organizacao_codigo INT NOT NULL,
    historico VARCHAR(150) NOT NULL,
    valor DECIMAL(12,2) NOT NULL,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (organizacao_codigo) REFERENCES organizacao(codigo)
);

INSERT INTO organizacao (codigo, nome_fantasia, restricoes) VALUES
(1, 'Banco Alfa', '2'),
(2, 'Banco Beta', '1'),
(3, 'Tech Gama', ''),
(4, 'Mercado Delta', '');

INSERT INTO operador (codigo, nome_acesso, credencial, perfil) VALUES
(1, 'admin', SHA2('admin123', 256), 'ADMIN'),
(2, 'maria', SHA2('123456', 256), 'COMUM'),
(3, 'joao', SHA2('123456', 256), 'COMUM'),
(4, 'admin2', SHA2('admin123', 256), 'ADMIN');

INSERT INTO operador_organizacao (operador_codigo, organizacao_codigo) VALUES
(1, 1), (1, 3),
(2, 1),
(3, 2),
(4, 2), (4, 4);