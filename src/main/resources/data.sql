--Hier moet ik data invullen voor de eindopdracht zodat dit niet met lege data wordt ingestuurd.
--insert patients
insert into patients(id, first_name, last_name, full_name)
values
    (nextval('patients_seq'), 'Peter', 'Pannenkoek', 'Peter Pannenkoek'),
    (nextval('patients_seq'), 'Linda', 'de Mol', 'Linda de Mol'),
    (nextval('patients_seq'), 'Lisa', 'Simpson', 'Lisa Simpson');

--insert employees
insert into employees(id, first_name, last_name, full_name, role)
values
    (nextval('employees_seq'), 'Karim', 'Kendal', 'Karim Kendal', 'Doctor'),
    (nextval('employees_seq'), 'Mickey', 'Mouse', 'Mickey Mouse', 'Doctor'),
    (nextval('employees_seq'), 'Pietje', 'Puk', 'Pietje Puk', 'Secretares');

--insert dossiers
insert into dossiers(id, dossier_is_closed, name)
values
    (nextval('dossiers_seq'), 'false', 'Peter Pannenkoek'),
    (nextval('dossiers_seq'), 'true', 'Linda de Mol'),
    (nextval('dossiers_seq'), 'false', 'Lisa Simpson');

--insert reports
insert into reports(id, body, date)
values
    --Peter Pannenkoek
    (nextval('reports_seq'), 'Hij maakt de hele tijd alleen maar grappen, hij ontwijkt zijn problemen.', '2023-01-01'),
    (nextval('reports_seq'), 'Minder grappen vandaag, hij heeft wel een scheetkussen op mijn stoel gelegd.', '2023-01-08'),
    (nextval('reports_seq'), 'Vandaag helemaal geen grappen, we zijn tot de kern gekomen. HIj is bijna klaar voor ontslag.', '2023-01-15'),
    --Linda de Mol
    (nextval('reports_seq'), 'Eigenlijk was de verwijzing onterecht, ze hoeft niet meer terug te komen. Ik kan haar niet verder helpen.', '2022-02-15'),
    --Lisa Simpson
    (nextval('reports_seq'), 'Haalt veel troost uit haar saxofoon en rijkt uit naar de wereld doormiddels van links gedachtegoed te promoveren. Dit komt waarschijnlijk door de slechte band met haar vader. Verdere gesprekken nodig.', '2024-03-01'),
    (nextval('reports_seq'), 'Heeft een moeilijke relatie met haar broer. Dit lijkt verband te hebben met jaloesie om de beroemdheid van Bart.', '2024-03-08');


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
  AND employees.full_name = 'Mickey Mouse';

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
  AND employees.full_name = 'Karim Kendal';

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
  AND employees.full_name = 'Mickey Mouse';

--link patients and employees
INSERT INTO employee_patients (patient_id, employee_id)
VALUES
    (
        (SELECT id FROM patients WHERE full_name = 'Linda de Mol'),
        (SELECT id FROM employees WHERE full_name = 'Pietje Puk')
    ),
    (
        (SELECT id FROM patients WHERE full_name = 'Peter Pannenkoek'),
        (SELECT id FROM employees WHERE full_name = 'Pietje Puk')
    ),
    (
        (SELECT id FROM patients WHERE full_name = 'Lisa Simpson'),
        (SELECT id FROM employees WHERE full_name = 'Pietje Puk')
    ),
    (
        (SELECT id FROM patients WHERE full_name = 'Linda de Mol'),
        (SELECT id FROM employees WHERE full_name = 'Karim Kendal')
    ),
    (
        (SELECT id FROM patients WHERE full_name = 'Peter Pannenkoek'),
        (SELECT id FROM employees WHERE full_name = 'Mickey Mouse')
    ),
    (
        (SELECT id FROM patients WHERE full_name = 'Lisa Simpson'),
        (SELECT id FROM employees WHERE full_name = 'Karim Kendal')
    );