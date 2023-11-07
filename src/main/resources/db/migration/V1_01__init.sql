create table if not exists users
(
    id         bigserial     not null primary key,
    name       varchar(255)  not null,
    email      varchar(320)  not null unique,
    username   varchar(50)   not null unique,
    password   varchar(255)  not null,
    avatar     varchar(2048) null,
    phone      varchar(20)   null unique,
    role_id    smallint      not null,
    is_blocked boolean       not null,
    last_login timestamp     null,
    created    timestamp     null
);

create table if not exists realtors_info
(
    id                        bigint        not null primary key
        constraint pk_users_id references users,
    agency                    varchar(50)   null,
    agency_site               varchar(2048) null,
    subscription_type         smallint      not null,
    announcement_count        integer       not null,
    public_announcement_count integer       not null
);

create index username_idx on users (username);

insert into users (name, email, username, password,
                   phone, avatar, role_id,
                   is_blocked, last_login, created)
values ('Admin', 'makurohashami@gmail.com', 'admin',
        '$2a$12$BPdZng5Zle584HCtKfxeoul4TiM0ngA/UyNCBcOsmsYqd.bdIMFaq',
        '+38(099)-000-00-00', 'https://i.imgur.com/meePOPU.png',
        2, false, null, now());