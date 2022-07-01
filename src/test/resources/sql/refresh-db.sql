DROP TABLE IF EXISTS user_model;

CREATE TABLE user_model
(
    id      BIGSERIAL PRIMARY KEY,
    first_name    VARCHAR,
    last_name   VARCHAR,
    age   INTEGER
);

INSERT INTO user_model(first_name, last_name, age)
VALUES ('Dima', 'Dimov', 16),
       ('Ivan', 'Ivanov', 23),
       ('Petr', 'Petrov', 19);