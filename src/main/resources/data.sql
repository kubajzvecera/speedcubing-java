MERGE INTO competitor (id, first_name, last_name, email, birth_date, country) KEY (id) VALUES (1, 'Jan', 'Novák', 'jan.novak@email.cz', '2000-03-15', 'Czechia');
MERGE INTO competitor (id, first_name, last_name, email, birth_date, country) KEY (id) VALUES (2, 'Petr', 'Svoboda', 'petr.svoboda@email.cz', '1998-07-22', 'Czechia');
MERGE INTO competitor (id, first_name, last_name, email, birth_date, country) KEY (id) VALUES (3, 'Lukas', 'Werner', 'lukas.werner@email.de', '2001-11-08', 'Germany');
MERGE INTO competitor (id, first_name, last_name, email, birth_date, country) KEY (id) VALUES (4, 'Mateusz', 'Kowalski', 'mateusz.kowalski@email.pl', '1999-05-30', 'Poland');
MERGE INTO competitor (id, first_name, last_name, email, birth_date, country) KEY (id) VALUES (5, 'Samuel', 'Jones', 'sam.jones@email.uk', '2002-01-14', 'UK');

MERGE INTO organizer (id, first_name, last_name, email) KEY (id) VALUES (1, 'Karel', 'Výborný', 'karel@cubes.cz');
MERGE INTO organizer (id, first_name, last_name, email) KEY (id) VALUES (2, 'Eva', 'Novotná', 'eva@speedcubing.cz');

MERGE INTO category (id, name) KEY (id) VALUES (1, '3x3x3');
MERGE INTO category (id, name) KEY (id) VALUES (2, '2x2x2');
MERGE INTO category (id, name) KEY (id) VALUES (3, '4x4x4');

MERGE INTO competition (id, name, date, location, end_date, organizer_id) KEY (id) VALUES (1, 'Czech Open 2025', '2025-06-15', 'Prague', '2025-06-16', 1);
MERGE INTO competition (id, name, date, location, end_date, organizer_id) KEY (id) VALUES (2, 'Summer Cubing 2025', '2025-08-20', 'Brno', NULL, 2);

MERGE INTO competition_category (competition_id, category_id) KEY (competition_id, category_id) VALUES (1, 1);
MERGE INTO competition_category (competition_id, category_id) KEY (competition_id, category_id) VALUES (1, 2);
MERGE INTO competition_category (competition_id, category_id) KEY (competition_id, category_id) VALUES (2, 1);
MERGE INTO competition_category (competition_id, category_id) KEY (competition_id, category_id) VALUES (2, 3);

MERGE INTO round (id, name, round_number, category_id, competition_id) KEY (id) VALUES (1, 'First Round', 1, 1, 1);
MERGE INTO round (id, name, round_number, category_id, competition_id) KEY (id) VALUES (2, 'Final', 2, 1, 1);
MERGE INTO round (id, name, round_number, category_id, competition_id) KEY (id) VALUES (3, 'First Round', 1, 2, 1);
MERGE INTO round (id, name, round_number, category_id, competition_id) KEY (id) VALUES (4, 'First Round', 1, 3, 1);

MERGE INTO registration (id, competition_id, competitor_id, registration_datetime) KEY (id) VALUES (1, 1, 1, '2025-06-01T10:00:00');
MERGE INTO registration (id, competition_id, competitor_id, registration_datetime) KEY (id) VALUES (2, 1, 2, '2025-06-01T10:05:00');
MERGE INTO registration (id, competition_id, competitor_id, registration_datetime) KEY (id) VALUES (3, 1, 3, '2025-06-01T10:10:00');
MERGE INTO registration (id, competition_id, competitor_id, registration_datetime) KEY (id) VALUES (4, 2, 3, '2025-08-01T09:00:00');
MERGE INTO registration (id, competition_id, competitor_id, registration_datetime) KEY (id) VALUES (5, 2, 4, '2025-08-01T09:05:00');
MERGE INTO registration (id, competition_id, competitor_id, registration_datetime) KEY (id) VALUES (6, 2, 5, '2025-08-01T09:10:00');

MERGE INTO registration_category (registration_id, category_id) KEY (registration_id, category_id) VALUES (1, 1);
MERGE INTO registration_category (registration_id, category_id) KEY (registration_id, category_id) VALUES (1, 2);
MERGE INTO registration_category (registration_id, category_id) KEY (registration_id, category_id) VALUES (2, 1);
MERGE INTO registration_category (registration_id, category_id) KEY (registration_id, category_id) VALUES (3, 1);
MERGE INTO registration_category (registration_id, category_id) KEY (registration_id, category_id) VALUES (3, 2);
MERGE INTO registration_category (registration_id, category_id) KEY (registration_id, category_id) VALUES (4, 1);
MERGE INTO registration_category (registration_id, category_id) KEY (registration_id, category_id) VALUES (5, 3);
MERGE INTO registration_category (registration_id, category_id) KEY (registration_id, category_id) VALUES (6, 1);
MERGE INTO registration_category (registration_id, category_id) KEY (registration_id, category_id) VALUES (6, 3);

-- Solves for Round 1 (3x3x3 First Round) - competitors 1,2,3 each 5 solves
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (1, 1, 5230, NULL, 1, 1);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (2, 2, 4890, NULL, 1, 1);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (3, 3, 5120, NULL, 1, 1);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (4, 4, 4980, NULL, 1, 1);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (5, 5, 5350, 'DNF', 1, 1);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (6, 1, 6120, NULL, 2, 1);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (7, 2, 5880, NULL, 2, 1);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (8, 3, 6050, NULL, 2, 1);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (9, 4, 5930, NULL, 2, 1);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (10, 5, 6210, NULL, 2, 1);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (11, 1, 4560, NULL, 3, 1);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (12, 2, 4720, NULL, 3, 1);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (13, 3, 4410, NULL, 3, 1);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (14, 4, 4680, NULL, 3, 1);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (15, 5, 4830, NULL, 3, 1);

-- Solves for Round 2 (3x3x3 Final) - competitors 1,2,3 each 5 solves
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (16, 1, 5100, NULL, 1, 2);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (17, 2, 4950, NULL, 1, 2);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (18, 3, 5230, NULL, 1, 2);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (19, 4, 4880, NULL, 1, 2);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (20, 5, 5060, NULL, 1, 2);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (21, 1, 5890, NULL, 2, 2);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (22, 2, 5720, NULL, 2, 2);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (23, 3, 6010, NULL, 2, 2);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (24, 4, 5840, NULL, 2, 2);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (25, 5, 5930, NULL, 2, 2);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (26, 1, 4410, NULL, 3, 2);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (27, 2, 4580, NULL, 3, 2);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (28, 3, 4350, NULL, 3, 2);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (29, 4, 4690, NULL, 3, 2);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (30, 5, 4520, NULL, 3, 2);

-- Solves for Round 3 (2x2x2 First Round) - competitors 1,3 each 5 solves
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (31, 1, 2350, NULL, 1, 3);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (32, 2, 2180, NULL, 1, 3);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (33, 3, 2410, NULL, 1, 3);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (34, 4, 2250, NULL, 1, 3);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (35, 5, 2120, NULL, 1, 3);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (36, 1, 1980, NULL, 3, 3);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (37, 2, 2050, NULL, 3, 3);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (38, 3, 1890, NULL, 3, 3);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (39, 4, 2120, NULL, 3, 3);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (40, 5, 1960, NULL, 3, 3);

-- Solves for Round 4 (4x4x4 First Round) - competitors 4,5 each 5 solves
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (41, 1, 8920, NULL, 4, 4);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (42, 2, 8750, NULL, 4, 4);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (43, 3, 9100, NULL, 4, 4);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (44, 4, 8830, NULL, 4, 4);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (45, 5, 8670, NULL, 4, 4);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (46, 1, 9450, NULL, 5, 4);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (47, 2, 9230, NULL, 5, 4);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (48, 3, 9370, NULL, 5, 4);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (49, 4, 9520, NULL, 5, 4);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (50, 5, 9180, NULL, 5, 4);

-- Third competition with more data
MERGE INTO competition (id, name, date, location, end_date, organizer_id) KEY (id) VALUES (3, 'Winter Cubing 2025', '2025-12-06', 'Ostrava', '2025-12-07', 1);
MERGE INTO competition_category (competition_id, category_id) KEY (competition_id, category_id) VALUES (3, 1);
MERGE INTO competition_category (competition_id, category_id) KEY (competition_id, category_id) VALUES (3, 2);
MERGE INTO competition_category (competition_id, category_id) KEY (competition_id, category_id) VALUES (3, 3);

-- More rounds for 2x2x2 and 4x4x4
MERGE INTO round (id, name, round_number, category_id, competition_id) KEY (id) VALUES (5, 'Final', 2, 2, 1);
MERGE INTO round (id, name, round_number, category_id, competition_id) KEY (id) VALUES (6, 'Final', 2, 3, 1);

-- Rounds for competition 2
MERGE INTO round (id, name, round_number, category_id, competition_id) KEY (id) VALUES (9, 'First Round', 1, 1, 2);
MERGE INTO round (id, name, round_number, category_id, competition_id) KEY (id) VALUES (10, 'Final', 2, 1, 2);
MERGE INTO round (id, name, round_number, category_id, competition_id) KEY (id) VALUES (11, 'First Round', 1, 3, 2);
MERGE INTO round (id, name, round_number, category_id, competition_id) KEY (id) VALUES (12, 'Final', 2, 3, 2);

-- Rounds for competition 3
MERGE INTO round (id, name, round_number, category_id, competition_id) KEY (id) VALUES (7, 'Qualification', 1, 1, 3);
MERGE INTO round (id, name, round_number, category_id, competition_id) KEY (id) VALUES (8, 'Semi-Final', 2, 1, 3);

-- Register more competitors for competition 3
MERGE INTO registration (id, competition_id, competitor_id, registration_datetime) KEY (id) VALUES (7, 3, 1, '2025-11-01T10:00:00');
MERGE INTO registration (id, competition_id, competitor_id, registration_datetime) KEY (id) VALUES (8, 3, 2, '2025-11-01T10:05:00');
MERGE INTO registration (id, competition_id, competitor_id, registration_datetime) KEY (id) VALUES (9, 3, 4, '2025-11-01T10:10:00');

MERGE INTO registration_category (registration_id, category_id) KEY (registration_id, category_id) VALUES (7, 1);
MERGE INTO registration_category (registration_id, category_id) KEY (registration_id, category_id) VALUES (7, 2);
MERGE INTO registration_category (registration_id, category_id) KEY (registration_id, category_id) VALUES (8, 1);
MERGE INTO registration_category (registration_id, category_id) KEY (registration_id, category_id) VALUES (9, 1);
MERGE INTO registration_category (registration_id, category_id) KEY (registration_id, category_id) VALUES (9, 3);

-- Solves for Round 5 (2x2x2 Final) - competitors 1,3 each 5 solves
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (51, 1, 2210, NULL, 1, 5);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (52, 2, 2090, NULL, 1, 5);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (53, 3, 2310, NULL, 1, 5);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (54, 4, 2150, NULL, 1, 5);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (55, 5, 2030, NULL, 1, 5);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (56, 1, 1850, NULL, 3, 5);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (57, 2, 1920, NULL, 3, 5);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (58, 3, 1780, NULL, 3, 5);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (59, 4, 1880, NULL, 3, 5);
MERGE INTO solve (id, attempt_number, time_ms, penalty, competitor_id, round_id) KEY (id) VALUES (60, 5, 1950, NULL, 3, 5);

ALTER TABLE competitor ALTER COLUMN id RESTART WITH 6;
ALTER TABLE organizer ALTER COLUMN id RESTART WITH 3;
ALTER TABLE category ALTER COLUMN id RESTART WITH 4;
ALTER TABLE competition ALTER COLUMN id RESTART WITH 4;
ALTER TABLE round ALTER COLUMN id RESTART WITH 13;
ALTER TABLE registration ALTER COLUMN id RESTART WITH 10;
ALTER TABLE solve ALTER COLUMN id RESTART WITH 61;
