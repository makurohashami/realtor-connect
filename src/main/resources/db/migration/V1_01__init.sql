create table if not exists users
(
 id         bigserial,
 email      varchar(255) not null unique,
 username   varchar(255) not null unique,
 password   varchar(255) not null,
 role_id    smallint     not null,
 is_blocked  boolean      not null,
 last_login timestamp    null,
 created    timestamp    null
);

create index username_idx on users (username);

insert into users (email, username, password, role_id, is_blocked, last_login, created)
 values ('makurohashami@gmail.com', 'admin', '$2a$12$BPdZng5Zle584HCtKfxeoul4TiM0ngA/UyNCBcOsmsYqd.bdIMFaq', 2, false, null, now());