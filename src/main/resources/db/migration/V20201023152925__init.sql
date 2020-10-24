CREATE TABLE company
(
    id              bigint          NOT NULL PRIMARY KEY auto_increment,
    company_name    VARCHAR(30)     NOT NULL
);
CREATE TABLE employee
(
    id              bigint          NOT NULL PRIMARY KEY auto_increment,
    name            VARCHAR(30)     NOT NULL,
    age             bigint,
    gender          VARCHAR(10),
    salary          bigint,
    company_id      bigint,
    FOREIGN KEY     (company_id)    REFERENCES company (id)
);
