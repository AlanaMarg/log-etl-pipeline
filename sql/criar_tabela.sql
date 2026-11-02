CREATE DATABASE loginsight;

--conecte no banco loginsight antes de rodar o create table

CREATE TABLE tb_log_entries (
	id SERIAL PRIMARY KEY,
	ip VARCHAR(45) NOT NULL,
	data_hora VARCHAR(45) NOT NULL,
	metodo VARCHAR(10)  NOT NULL,
	endpoint VARCHAR(255) NOT NULL,
	status_code INT NOT NULL
);