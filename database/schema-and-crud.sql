-- Database name: neighborhood_security_alert_system
-- Run CREATE DATABASE separately while connected to the default postgres database:
CREATE DATABASE neighborhood_security_alert_system;

-- Hibernate ddl-auto=update creates the schema from the JPA entities.
-- The following is a reference SQL schema / CRUD guide if you want to run SQL directly.
-- Execute the table statements after connecting to neighborhood_security_alert_system.

CREATE TABLE IF NOT EXISTS patrol_zone (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    zone_number INTEGER NOT NULL,
    name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS incident_type (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS security_guard (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    phone VARCHAR(255),
    patrol_zone_id UUID REFERENCES patrol_zone(id)
);

CREATE TABLE IF NOT EXISTS security_guard_incident_type (
    security_guard_id UUID NOT NULL REFERENCES security_guard(id) ON DELETE CASCADE,
    incident_type_id UUID NOT NULL REFERENCES incident_type(id) ON DELETE CASCADE,
    PRIMARY KEY (security_guard_id, incident_type_id)
);

CREATE TABLE IF NOT EXISTS resident (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    national_id VARCHAR(255) UNIQUE,
    phone VARCHAR(255),
    street VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS incident_report (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    report_date DATE,
    status VARCHAR(30) NOT NULL,
    description VARCHAR(255),
    location VARCHAR(255),
    resident_id UUID REFERENCES resident(id),
    security_guard_id UUID REFERENCES security_guard(id)
);

CREATE TABLE IF NOT EXISTS response_action (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    action_name VARCHAR(255),
    notes VARCHAR(255),
    minutes_taken INTEGER NOT NULL DEFAULT 0,
    action_date DATE,
    incident_report_id UUID NOT NULL REFERENCES incident_report(id)
);

-- CRUD EXAMPLES: replace UUIDs with values from your own database/API responses.
-- CREATE
INSERT INTO patrol_zone(zone_number, name) VALUES (1, 'Central Zone');
INSERT INTO incident_type(name) VALUES ('Theft');

-- READ
SELECT * FROM patrol_zone;
SELECT * FROM incident_type;
SELECT * FROM security_guard;
SELECT * FROM resident;
SELECT * FROM incident_report;
SELECT * FROM response_action;

-- UPDATE
UPDATE patrol_zone SET name='Central Patrol Zone' WHERE zone_number=1;
UPDATE resident SET phone='0780000000' WHERE national_id='1200180045678901';

-- DELETE (delete dependent rows first because of foreign keys)
DELETE FROM response_action WHERE incident_report_id = '00000000-0000-0000-0000-000000000000';
DELETE FROM incident_report WHERE id = '00000000-0000-0000-0000-000000000000';
DELETE FROM resident WHERE national_id='1200180045678901';

-- RELATIONSHIP EXAMPLES
-- Guard with assigned zone:
SELECT g.first_name, g.last_name, z.zone_number, z.name
FROM security_guard g LEFT JOIN patrol_zone z ON z.id = g.patrol_zone_id;

-- Guard incident types (many-to-many):
SELECT g.first_name, g.last_name, t.name
FROM security_guard g
JOIN security_guard_incident_type gt ON gt.security_guard_id=g.id
JOIN incident_type t ON t.id=gt.incident_type_id;

-- Reports with resident and guard:
SELECT i.id, i.report_date, i.status, r.first_name AS resident_first_name,
       g.first_name AS guard_first_name
FROM incident_report i
LEFT JOIN resident r ON r.id=i.resident_id
LEFT JOIN security_guard g ON g.id=i.security_guard_id;

-- Response actions for each report:
SELECT a.action_name, a.action_date, i.id AS report_id
FROM response_action a JOIN incident_report i ON i.id=a.incident_report_id;
