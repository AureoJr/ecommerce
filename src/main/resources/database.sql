DROP DATABASE IF EXISTS ecommerce;
CREATE DATABASE IF NOT EXISTS ecommerce;

use ecommerce;

CREATE TABLE category(
  id integer unsigned not null AUTO_INCREMENT,
  enabled BOOLEAN default TRUE,
  name varchar(60),
  PRIMARY KEY (id)
);

create TABLE  product (
  id integer unsigned not null AUTO_INCREMENT,
  name VARCHAR(60),
  quantityAvailable integer,
  size VARCHAR(30),
  color VARCHAR(400),
  weight VARCHAR(30),
  description VARCHAR(4000),
  price DOUBLE(10,2),
  mainCategory INTEGER UNSIGNED,
  enabled boolean,
  checekd boolean DEFAULT false,
  defaultImage VARCHAR(400) default '/img/semimagem.jpg',
  PRIMARY KEY (id)

);