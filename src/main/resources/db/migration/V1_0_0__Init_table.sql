CREATE TABLE IF NOT EXISTS task
(
    id          SERIAL PRIMARY KEY,
    created     TIMESTAMP    NOT NULL,
    last_upd    TIMESTAMP    NOT NULL,
    title       VARCHAR(200) NOT NULL CHECK (length(trim(description)) >= 3),
    description VARCHAR(2000),
    due_date    TIMESTAMP    NOT NULL,
    completed   VARCHAR(10)  NOT NULL CHECK (length(trim(description)) >= 1)
);
