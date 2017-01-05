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
  color VARCHAR(400),
  weight VARCHAR(30),
  description VARCHAR(4000),
  price DOUBLE,
  mainCategory INTEGER UNSIGNED,
  enabled boolean

);

  insert into product (name,quantityAvailable,size,color,weight,description,price,mainCategory,enabled)
       VALUES
         ('koenigsegg One:1',
         7,
         '4,5mx2,06mx1,15m',
         '#fff,#000|#F34,#000',
         '1360 KG',
         'The One:1 was introduced in 2014. Seven examples, including one prototype, were built during 2014 and 2015.',
          9305818.59,
         1,
         true)