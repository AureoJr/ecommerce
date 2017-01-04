CREATE DATABASE ecommerce;

CREATE TABLE category(
  id integer unsigned PRIMARY KEY not null,
  enable BOOLEAN default TRUE,
  name varchar(60)
);

create TABLE  product (
  id integer unsigned PRIMARY KEY not null,
  name VARCHAR(60),
  quantityAvailable integer,
  size VARCHAR(30),
  color VARCHAR(30),
  weight VARCHAR(30),
  description VARCHAR(4000),
  price DOUBLE,
  mainCategory INTEGER UNSIGNED,
  enabled boolean

)