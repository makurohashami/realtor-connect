CREATE TABLE IF NOT EXISTS real_estates
(
    id                   BIGSERIAL        NOT NULL PRIMARY KEy,
    name                 VARCHAR(255)     NOT NULL,
    description          VARCHAR(512)     NOT NULL,
    price                NUMERIC(20, 2)   NOT NULL,
    verified             BOOLEAN          NOT NULL,
    owner_name           VARCHAR(255)     NOT NULL,
    owner_phone          VARCHAR(20)      NOT NULL,
    owner_email          VARCHAR(320)     NULL,
    city                 VARCHAR(255)     NOT NULL,
    district             VARCHAR(255)     NOT NULL,
    residential_area     VARCHAR(255)     NOT NULL,
    street               VARCHAR(255)     NOT NULL,
    housing_estate       VARCHAR(255)     NULL,
    house_number         INTEGER          NOT NULL,
    block                VARCHAR(50)      NULL,
    apartment_number     INTEGER          NULL,
    landmark             VARCHAR(255)     NULL,
    loggia_type_id       INTEGER          NULL,
    loggias_count        SMALLINT         NULL,
    is_loggia_glassed    BOOLEAN          NULL,
    bathroom_type_id     INTEGER          NOT NULL,
    bathrooms_count      SMALLINT         NOT NULL,
    is_bathroom_combined BOOLEAN          NOT NULL,
    total_area           DOUBLE PRECISION NOT NULL,
    living_area          DOUBLE PRECISION NOT NULL,
    kitchen_area         DOUBLE PRECISION NOT NULL,
    floor                SMALLINT         NOT NULL,
    floors_in_building   SMALLINT         NOT NULL,
    building_type_id     INTEGER          NOT NULL,
    heating_type_id      INTEGER          NOT NULL,
    windows_type_id      INTEGER          NOT NULL,
    hot_water_type_id    INTEGER          NOT NULL,
    state_type_id        INTEGER          NOT NULL,
    announcement_type_id INTEGER          NOT NULL,
    rooms_count          SMALLINT         NOT NULL,
    ceiling_height       DOUBLE PRECISION NULL,
    documents            VARCHAR(512)     NULL,
    is_private           BOOLEAN          NOT NULL,
    is_called            BOOLEAN          NOT NULL,
    called_at            TIMESTAMP(6)     NULL,
    created_at           TIMESTAMP(6)     NULL,
    realtor_id           BIGINT           NOT NULL
        CONSTRAINT to_realtor_id
            REFERENCES realtors_info
);

CREATE TABLE IF NOT EXISTS real_estates_photos
(
    id             BIGSERIAL     NOT NULL PRIMARY KEY,
    is_private     BOOLEAN       NOT NULL,
    photo          VARCHAR(2048) NULL,
    photo_id       VARCHAR(512)  NULL,
    order_num      BIGINT        NOT NULL,
    real_estate_id BIGINT        NOT NULL
        CONSTRAINT to_real_estate_id
            REFERENCES real_estates
);
