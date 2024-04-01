-- Default password: pass
INSERT INTO users (name, email, username, password, phone, role_id, is_blocked, last_login, created_at, created_by,
                   email_verified)
VALUES ('RC Admin', 'rc.admin@mail.com', 'rc.admin', '$2a$12$74O/v7yGpSI.JkRmIXn.m.TFhJmR.ztlY8tFNvzQOlr8DaWAg8x9.',
        '+380990000000', 0, FALSE, NULL, NOW() AT TIME ZONE 'UTC', 'defaultUser', TRUE),
       ('Admin', 'admin@mail.com', 'admin', '$2a$12$74O/v7yGpSI.JkRmIXn.m.TFhJmR.ztlY8tFNvzQOlr8DaWAg8x9.',
        '+380990000001', 10, FALSE, NULL, NOW() AT TIME ZONE 'UTC', 'defaultUser', TRUE),
       ('Realtor', 'realtor@mail.com', 'realtor', '$2a$12$74O/v7yGpSI.JkRmIXn.m.TFhJmR.ztlY8tFNvzQOlr8DaWAg8x9.',
        '+380990000002', 20, FALSE, NULL, NOW() AT TIME ZONE 'UTC', 'defaultUser', TRUE),
       ('User', 'user@mail.com', 'user', '$2a$12$74O/v7yGpSI.JkRmIXn.m.TFhJmR.ztlY8tFNvzQOlr8DaWAg8x9.',
        '+380990000003', 30, FALSE, NULL, NOW() AT TIME ZONE 'UTC', 'defaultUser', TRUE);
;

INSERT INTO realtors_info (id, agency, agency_site, subscription_type, public_real_estates_count,
                           notified_days_to_expire_prem)
VALUES ((SELECT id FROM users WHERE username = 'realtor'), 'agency', 'agency.com', 0, 0, NULL);

INSERT INTO realtors_contacts(contact, type_id, realtor_id)
VALUES ('https://t.me/realtor', 1, (SELECT id FROM users WHERE username = 'realtor')),
       ('realtor@mail.com', 4, (SELECT id FROM users WHERE username = 'realtor')),
       ('+380990000002', 0, (SELECT id FROM users WHERE username = 'realtor'));
