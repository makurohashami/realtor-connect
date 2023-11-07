create table if not exists real_estates
(
    id                   bigserial        not null primary key,
    name                 varchar(255)     not null,
    description          varchar(512)     null,
    price                numeric(38, 2)   not null,
    verified             boolean          not null,
    owner_name           varchar(255)     not null,
    owner_phone          varchar(20)      not null,
    owner_email          varchar(320)     null,
    city                 varchar(255)     not null,
    district             varchar(255)     not null,
    residential_area     varchar(255)     not null,
    street               varchar(255)     not null,
    housing_estate       varchar(255)     null,
    house_number         integer          not null,
    block                varchar(255)     null,
    apartment_number     integer          null,
    landmark             varchar(255)     null,
    loggia_type_id       integer          null,
    loggias_count        smallint         null,
    is_loggia_glassed    boolean          null,
    bathroom_type_id     integer          not null,
    bathrooms_count      smallint         not null,
    is_bathroom_combined boolean          not null,
    total_area           double precision not null,
    living_area          double precision not null,
    kitchen_area         double precision not null,
    floor                smallint         not null,
    floors_in_building   smallint         not null,
    building_type_id     integer          not null,
    heating_type_id      integer          not null,
    windows_type_id      integer          not null,
    hot_water_type_id    integer          not null,
    state_type_id        integer          not null,
    announcement_type_id integer          not null,
    rooms_count          smallint         not null,
    ceiling_height       double precision null,
    documents            varchar(512)     null,
    count_photos         smallint         not null,
    count_public_photos  smallint         not null,
    is_private           boolean          not null,
    called               boolean          not null,
    called_at            timestamp(6)     null,
    realtor_id           bigint           not null
        constraint to_realtor_id
            references realtors_info
);

create table if not exists real_estates_photos
(
    id             bigserial not null primary key,
    is_private     boolean   not null,
    photo          varchar(2048),
    real_estate_id bigint    not null
        constraint to_real_estate_id
            references real_estates
);
