--insert patients
insert into patients(id, first_name, last_name, full_name, next_appointment)
values
    (nextval('patients_generator'), 'Peter', 'Pannenkoek', 'Peter Pannenkoek', '2024-01-10'),
    (nextval('patients_generator'), 'Linda', 'de Mol', 'Linda de Mol', null),
    (nextval('patients_generator'), 'Lisa', 'Simpson', 'Lisa Simpson', '2024-04-05');

--insert employees
insert into employees(id, first_name, last_name, username, function, password)
values
    (nextval('employees_generator'),
     'Karim', 'Kendal', 'Karim.Kendal', 'Doctor',
     '$2a$12$kFsPDPZX4zuoifPtXEvyCucB0Ric3F7gkgksA4f5HvNG0SMVECsFW'
    ),
    (nextval('employees_generator'), 'Mickey', 'Mouse', 'Mickey.Mouse', 'Doctor',
     '$2a$12$VqnS8Ky0LnqXek1FGCzBEeF.Oj/TbCSR0eNPGmpwi37bPvHFM8Fny'
    ),
    (nextval('employees_generator'), 'Pietje', 'Puk', 'Pietje.Puk', 'Secretary',
     '$2a$12$HY/pP/ojBKSk8t.xDdFpJeHugn4YQHbRIGRd8I8fnnNh407SIJNCe'
    );

--insert dossiers
insert into dossiers(id, dossier_is_closed, name)
values
    (nextval('dossiers_generator'), 'false', 'Peter Pannenkoek'),
    (nextval('dossiers_generator'), 'true', 'Linda de Mol'),
    (nextval('dossiers_generator'), 'false', 'Lisa Simpson');

--insert reports
insert into reports(id, body, date)
values
    --Peter Pannenkoek
    (nextval('reports_generator'), 'Hij maakt de hele tijd alleen maar grappen, hij ontwijkt zijn problemen.', '2023-01-01'),
    (nextval('reports_generator'), 'Minder grappen vandaag, hij heeft wel een scheetkussen op mijn stoel gelegd.', '2023-01-08'),
    (nextval('reports_generator'), 'Vandaag helemaal geen grappen, we zijn tot de kern gekomen. HIj is bijna klaar voor ontslag.', '2023-01-15'),
    --Linda de Mol
    (nextval('reports_generator'), 'Eigenlijk was de verwijzing onterecht, ze hoeft niet meer terug te komen. Ik kan haar niet verder helpen.', '2022-02-15'),
    --Lisa Simpson
    (nextval('reports_generator'), 'Haalt veel troost uit haar saxofoon en rijkt uit naar de wereld doormiddels van links gedachtegoed te promoveren. Dit komt waarschijnlijk door de slechte band met haar vader. Verdere gesprekken nodig.', '2024-03-01'),
    (nextval('reports_generator'), 'Heeft een moeilijke relatie met haar broer. Dit lijkt verband te hebben met jaloesie om de beroemdheid van Bart.', '2024-03-08');

-- insert roles
insert into roles(rolename)
values ('ROLE_ADMIN'),
       ('ROLE_USER');

MERGE INTO patients p
    USING dossiers d
    ON p.full_name = d.name
    WHEN MATCHED THEN
        UPDATE SET p.dossier_id = d.id;

-- link reports to dossiers Peter Pannenkoek
UPDATE reports r
SET dossier_id = (SELECT id FROM dossiers WHERE name = 'Peter Pannenkoek')
WHERE r.date >= '2023-01-01' AND r.date <= '2023-12-31';

-- link reports to employee Peter Pannenkoek
UPDATE reports r
SET employees_id = (SELECT id FROM employees WHERE username = 'Mickey.Mouse')
WHERE r.date >= '2023-01-01' AND r.date <= '2023-12-31';

-- link reports to dossier Linda de Mol
UPDATE reports r
SET dossier_id = (SELECT id FROM dossiers WHERE name = 'Linda de Mol')
WHERE r.date >= '2022-01-01' AND r.date <= '2022-12-31';

-- link reports to employee Linda de Mol
UPDATE reports r
SET employees_id = (SELECT id FROM employees WHERE username = 'Karim.Kendal')
WHERE r.date >= '2022-01-01' AND r.date <= '2022-12-31';

-- link reports to dossier Lisa Simpson
UPDATE reports r
SET dossier_id = (SELECT id FROM dossiers WHERE name = 'Lisa Simpson')
WHERE r.date >= '2024-01-01' AND r.date <= '2024-12-31';

-- link reports to employee Lisa Simpson
UPDATE reports r
SET employees_id = (SELECT id FROM employees WHERE username = 'Mickey.Mouse')
WHERE r.date >= '2024-01-01' AND r.date <= '2024-12-31';

-- link patients and employees
INSERT INTO employee_patients (patient_id, employee_id)
SELECT p.id, e.id
FROM patients p
         CROSS JOIN employees e
WHERE (p.full_name = 'Linda de Mol' AND e.username = 'Pietje.Puk')
   OR (p.full_name = 'Peter Pannenkoek' AND e.username = 'Pietje.Puk')
   OR (p.full_name = 'Lisa Simpson' AND e.username = 'Pietje.Puk')
   OR (p.full_name = 'Linda de Mol' AND e.username = 'Karim.Kendal')
   OR (p.full_name = 'Peter Pannenkoek' AND e.username = 'Mickey.Mouse')
   OR (p.full_name = 'Lisa Simpson' AND e.username = 'Karim.Kendal');

-- set roles
INSERT INTO employees_roles (employees_id, roles_rolename)
SELECT e.id, r.rolename
FROM employees e
         CROSS JOIN roles r
WHERE (e.username = 'Pietje.Puk' AND r.rolename = 'ROLE_ADMIN')
   OR (e.username = 'Mickey.Mouse' AND r.rolename = 'ROLE_USER')
   OR (e.username = 'Karim.Kendal' AND r.rolename = 'ROLE_USER');