# API requests
### POST
Employees - endpoint /employees

[//]: # (Dossiers - endpoint / dossiers)

Reports - endpoint / reports/{dossierId}
*reports can't be added when dossier is closed*

Patients - endpoint / patients -
*will also create dossier*

### GET
Employees - endpoint /employees

Employee By username /employees/{username}

Dossiers - endpoint / dossiers/name

Dossier by name / dossiers/

Reports - endpoint / reports

Patients - endpoint / patients
patients by employee employees/patientid

patient - next appointment - /patients/appointment



### Put

Dossier - isDossierClosed /dossiers/open
*muse be done by secretary*
*reports can't be added when dossier is closed*

patient - set next appointment - /patients/appointment
*add the keys:String nextAppointment, Long patientId, String employeeUsername*



### Delete
Employee By username /employees{username}

Patient By ID /patients/{id} *will also delete dossier associated with the patient*

Report By ID /reports/{id}

