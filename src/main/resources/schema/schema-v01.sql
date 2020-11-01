DROP TABLE purchase_order IF EXISTS;

CREATE TABLE purchase_order  (
 id BIGINT IDENTITY NOT NULL PRIMARY KEY,
 orderid BIGINT,
 item VARCHAR(20),
 quantity BIGINT,
 vendor varchar(20)
);

