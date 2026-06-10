INSERT INTO competitor (name, country) VALUES ('Jan Novák', 'Czechia');
INSERT INTO competitor (name, country) VALUES ('Petr Svoboda', 'Czechia');
INSERT INTO competitor (name, country) VALUES ('Lukas Werner', 'Germany');
INSERT INTO competitor (name, country) VALUES ('Mateusz Kowalski', 'Poland');
INSERT INTO competitor (name, country) VALUES ('Samuel Jones', 'UK');

INSERT INTO competition (name, date, location) VALUES ('Czech Open 2025', '2025-06-15', 'Prague');
INSERT INTO competition (name, date, location) VALUES ('Summer Cubing 2025', '2025-08-20', 'Brno');

INSERT INTO round (name, round_number, competition_id) VALUES ('First Round', 1, 1);
INSERT INTO round (name, round_number, competition_id) VALUES ('Final', 2, 1);
INSERT INTO round (name, round_number, competition_id) VALUES ('First Round', 1, 2);
