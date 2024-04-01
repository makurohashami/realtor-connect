CREATE TABLE IF NOT EXISTS users
(
    id             BIGSERIAL     NOT NULL PRIMARY KEY,
    name           VARCHAR(255)  NOT NULL,
    email          VARCHAR(320)  NOT NULL UNIQUE,
    username       VARCHAR(50)   NOT NULL UNIQUE,
    password       VARCHAR(255)  NOT NULL,
    avatar         VARCHAR(2048) NULL,
    avatar_id      VARCHAR(512)  NULL,
    phone          VARCHAR(20)   NULL UNIQUE,
    role_id        INTEGER       NOT NULL,
    is_blocked     BOOLEAN       NOT NULL,
    last_login     TIMESTAMP     NULL,
    email_verified BOOLEAN       NOT NULL,
    created_at     TIMESTAMP(6)  NOT NULL,
    created_by     VARCHAR(50)   NOT NULL,
    updated_at     TIMESTAMP(6)  NULL,
    updated_by     VARCHAR(50)   NULL
);

CREATE INDEX IF NOT EXISTS username_idx ON users (username);

CREATE TABLE IF NOT EXISTS confirmation_tokens
(
    token   UUID NOT NULL PRIMARY KEY,
    user_id BIGSERIAL UNIQUE REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS realtors_info
(
    id                           BIGINT        NOT NULL PRIMARY KEY
        CONSTRAINT pk_users_id REFERENCES users,
    agency                       VARCHAR(50)   NULL,
    agency_site                  VARCHAR(2048) NULL,
    subscription_type            INTEGER       NOT NULL,
    public_real_estates_count    INTEGER       NOT NULL,
    premium_expires_at           TIMESTAMP     NULL,
    notified_days_to_expire_prem INTEGER       NULL
);
