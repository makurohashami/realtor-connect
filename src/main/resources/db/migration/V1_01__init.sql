create table if not exists users
(
    id             bigserial     not null primary key,
    name           varchar(255)  not null,
    email          varchar(320)  not null unique,
    username       varchar(50)   not null unique,
    password       varchar(255)  not null,
    avatar         varchar(2048) null,
    avatar_id      varchar(512)  null,
    phone          varchar(20)   null unique,
    role_id        integer       not null,
    is_blocked     boolean       not null,
    last_login     timestamp     null,
    created        timestamp     null,
    email_verified boolean       not null
);

create index username_idx on users (username);

create table if not exists confirmation_tokens
(
    token   UUID not null primary key,
    user_id bigserial unique references users (id)
);

create table if not exists realtors_info
(
    id                           bigint        not null primary key
        constraint pk_users_id references users,
    agency                       varchar(50)   null,
    agency_site                  varchar(2048) null,
    subscription_type            integer       not null,
    public_real_estates_count    integer       not null,
    premium_expires_at           timestamp     null,
    notified_days_to_expire_prem integer       null
);

insert into users (name, email, username, password,
                   phone, avatar, role_id,
                   is_blocked, last_login, created, email_verified)
values ('RC Admin', 'makurohashami@gmail.com', 'rc.admin',
        '$2a$12$BPdZng5Zle584HCtKfxeoul4TiM0ngA/UyNCBcOsmsYqd.bdIMFaq',
        '+380990000000', 'https://i.imgur.com/meePOPU.png',
        0, false, null, now() at time zone 'UTC', true);