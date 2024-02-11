INSERT INTO users (name, email, username, password, phone, role_id, is_blocked, last_login, created, email_verified)
VALUES ('Admin', 'admin@mail.com', 'admin.user', '$2a$12$b0dV3CqizJ2WQ80BL7jEjuEP3n.h.GcChwvGkTph6DZMuXLcERsim',
        '+380990000001', 10, false, null, now() at time zone 'UTC', true),
       ('Realtor', 'realtor@mail.com', 'realtor', '$2a$12$/XjPyFlvyaq2cFGqcIJt4Olthud.8eaNDwbKv96fWCACx6MX/oL.S',
        '+380990000002', 20, false, null, now() at time zone 'UTC', true),
       ('User', 'user@mail.com', 'user', '$2a$12$otfiy99HhHX/1sHaYjxNU.gK6a2UqXhbLfhpVi6EgE2jMa5dnTFne',
        '+380990000003', 30, false, null, now() at time zone 'UTC', true);

INSERT INTO realtors_info (id, agency, agency_site, subscription_type, public_real_estates_count, notified_days_to_expire_prem)
VALUES ((SELECT id FROM users WHERE username = 'realtor'), 'agency', 'agency.com', 0, 0, null);

INSERT INTO realtors_contacts(contact, type_id, realtor_id)
VALUES ('https://t.me/realtor', 1, (SELECT id FROM users WHERE username = 'realtor')),
       ('realtor@mail.com', 4, (SELECT id FROM users WHERE username = 'realtor')),
       ('+380990000002', 0, (SELECT id FROM users WHERE username = 'realtor'));
