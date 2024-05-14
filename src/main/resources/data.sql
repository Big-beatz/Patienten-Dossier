--insert patients
insert into patients(id, first_name, last_name, full_name)
values
    (nextval('patients_generator'), 'Peter', 'Pannenkoek', 'Peter Pannenkoek'),
    (nextval('patients_generator'), 'Linda', 'de Mol', 'Linda de Mol'),
    (nextval('patients_generator'), 'Lisa', 'Simpson', 'Lisa Simpson');

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
    (nextval('employees_generator'), 'Pietje', 'Puk', 'Pietje.Puk', 'Secretares',
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

--link dossiers to patients
update patients
set dossier_id = dossiers.id
from dossiers
where dossiers.name = patients.full_name;

--link reports to dossiers Peter Pannenkoek
update reports
set dossier_id = dossiers.id
from dossiers
where reports.date >= '2023-01-01' AND reports.date <= '2023-12-31'
AND dossiers.name = 'Peter Pannenkoek';

--link reports to employee Peter Pannenkoek
update reports
set employees_id = employees.id
from employees
where reports.date >= '2023-01-01' AND reports.date <= '2023-12-31'
  AND employees.username = 'Mickey.Mouse';

--link reports to dossier Linda de Mol
update reports
set dossier_id = dossiers.id
from dossiers
where reports.date >= '2022-01-01' AND reports.date <= '2022-12-31'
  AND dossiers.name = 'Linda de Mol';

--link reports to employee Linda de Mol
update reports
set employees_id = employees.id
from employees
where reports.date >= '2022-01-01' AND reports.date <= '2022-12-31'
  AND employees.username = 'Karim.Kendal';

--link reports to dossier Lisa Simpson
update reports
set dossier_id = dossiers.id
from dossiers
where reports.date >= '2024-01-01' AND reports.date <= '2024-12-31'
  AND dossiers.name = 'Lisa Simpson';

--link reports to employee Lisa Simpson
update reports
set employees_id = employees.id
from employees
where reports.date >= '2024-01-01' AND reports.date <= '2024-12-31'
  AND employees.username = 'Mickey.Mouse';

--link patients and employees
INSERT INTO employee_patients (patient_id, employee_id)
VALUES
    (
        (SELECT id FROM patients WHERE full_name = 'Linda de Mol'),
        (SELECT id FROM employees WHERE username = 'Pietje.Puk')
    ),
    (
        (SELECT id FROM patients WHERE full_name = 'Peter Pannenkoek'),
        (SELECT id FROM employees WHERE username = 'Pietje.Puk')
    ),
    (
        (SELECT id FROM patients WHERE full_name = 'Lisa Simpson'),
        (SELECT id FROM employees WHERE username = 'Pietje.Puk')
    ),
    (
        (SELECT id FROM patients WHERE full_name = 'Linda de Mol'),
        (SELECT id FROM employees WHERE username = 'Karim.Kendal')
    ),
    (
        (SELECT id FROM patients WHERE full_name = 'Peter Pannenkoek'),
        (SELECT id FROM employees WHERE username = 'Mickey.Mouse')
    ),
    (
        (SELECT id FROM patients WHERE full_name = 'Lisa Simpson'),
        (SELECT id FROM employees WHERE username = 'Karim.Kendal')
    );

-- set roles
INSERT INTO employees_roles(employees_id, roles_rolename)
VALUES (
        (SELECT id FROM employees WHERE username = 'Pietje.Puk'),
        (SELECT rolename FROM roles WHERE rolename = 'ROLE_ADMIN')
       ),
       (
       (SELECT id FROM employees WHERE username = 'Mickey.Mouse'),
       (SELECT rolename FROM roles WHERE rolename = 'ROLE_USER')
       ),
       (
       (SELECT id FROM employees WHERE username = 'Karim.Kendal'),
       (SELECT rolename FROM roles WHERE rolename = 'ROLE_USER')
       );