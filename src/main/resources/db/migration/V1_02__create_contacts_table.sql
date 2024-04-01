CREATE TABLE IF NOT EXISTS realtors_contacts
(
    id         BIGSERIAL     NOT NULL PRIMARY KEY,
    contact    VARCHAR(2048) NOT NULL,
    type_id    INTEGER       NOT NULL,
    realtor_id BIGINT        NOT NULL,
    created_at TIMESTAMP(6)  NOT NULL,
    created_by VARCHAR(50)   NOT NULL,
    updated_at TIMESTAMP(6)  NULL,
    updated_by VARCHAR(50)   NULL,
    CONSTRAINT fk_to_realtor_id
        FOREIGN KEY (realtor_id) REFERENCES realtors_info (id)
);
