CREATE TABLE IF NOT EXISTS realtors_contacts
(
    id         BIGSERIAL     NOT NULL PRIMARY KEY,
    contact    VARCHAR(2048) NOT NULL,
    type_id    INTEGER       NOT NULL,
    realtor_id BIGINT        NOT NULL
        CONSTRAINT fk_to_realtor_id
            REFERENCES realtors_info
);
