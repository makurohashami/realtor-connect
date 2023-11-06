create table realtors_contacts
(
 id         bigserial     not null primary key,
 contact    varchar(2048) not null,
 type_id    smallint      not null,
 realtor_id bigint        not null
 constraint fk_to_realtor_id
 references realtors_info
);