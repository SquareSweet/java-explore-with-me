CREATE TABLE IF NOT EXISTS endpoint_hits (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY,
    app VARCHAR(200) NOT NULL,
    uri VARCHAR(200) NOT NULL,
    ip VARCHAR(15) NOT NULL,
    hit_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_endpoint_hits PRIMARY KEY (id)
);