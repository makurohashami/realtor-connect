-- Default password: pass
INSERT INTO users (name, email, username, password, phone, role_id, is_blocked, last_login, created_at, created_by,
                   email_verified, avatar)
VALUES ('RC Admin', 'rc.admin@mail.com', 'rc.admin', '$2a$12$74O/v7yGpSI.JkRmIXn.m.TFhJmR.ztlY8tFNvzQOlr8DaWAg8x9.',
        '+380990000000', 0, FALSE, NULL, NOW() AT TIME ZONE 'UTC', 'defaultUser', TRUE,
        'https://res.cloudinary.com/dhseztjx1/image/upload/v1711873406/realtor-connect-common/logos/realtor_connect_logo_jpg.jpg'),
       ('Admin', 'admin@mail.com', 'admin', '$2a$12$74O/v7yGpSI.JkRmIXn.m.TFhJmR.ztlY8tFNvzQOlr8DaWAg8x9.',
        '+380990000001', 10, FALSE, NULL, NOW() AT TIME ZONE 'UTC', 'defaultUser', TRUE,
        'https://res.cloudinary.com/dhseztjx1/image/upload/v1711873406/realtor-connect-common/logos/realtor_connect_logo_jpg.jpg'),
       ('Realtor', 'realtor@mail.com', 'realtor', '$2a$12$74O/v7yGpSI.JkRmIXn.m.TFhJmR.ztlY8tFNvzQOlr8DaWAg8x9.',
        '+380990000002', 20, FALSE, NULL, NOW() AT TIME ZONE 'UTC', 'defaultUser', TRUE,
        'https://res.cloudinary.com/dhseztjx1/image/upload/v1711873406/realtor-connect-common/logos/realtor_connect_logo_jpg.jpg'),
       ('User', 'user@mail.com', 'user', '$2a$12$74O/v7yGpSI.JkRmIXn.m.TFhJmR.ztlY8tFNvzQOlr8DaWAg8x9.',
        '+380990000003', 30, FALSE, NULL, NOW() AT TIME ZONE 'UTC', 'defaultUser', TRUE,
        'https://res.cloudinary.com/dhseztjx1/image/upload/v1711873406/realtor-connect-common/logos/realtor_connect_logo_jpg.jpg');
;

INSERT INTO realtors_info (id, agency, agency_site, subscription_type, public_real_estates_count,
                           notified_days_to_expire_prem)
VALUES ((SELECT id FROM users WHERE username = 'realtor'), 'agency', 'agency.com', 0, 0, NULL);

INSERT INTO realtors_contacts(contact, type_id, realtor_id, created_at, created_by)
VALUES ('https://t.me/realtor', 1, (SELECT id FROM users WHERE username = 'realtor'), NOW() AT TIME ZONE 'UTC',
        'defaultUser'),
       ('realtor@mail.com', 4, (SELECT id FROM users WHERE username = 'realtor'), NOW() AT TIME ZONE 'UTC',
        'defaultUser'),
       ('+380990000002', 0, (SELECT id FROM users WHERE username = 'realtor'), NOW() AT TIME ZONE 'UTC', 'defaultUser');
