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

patient - next appointment - /patients{id}/{nextAppointment}



### Update

Dossier - isDossierClosed /dossiers{id}/{isDossierClosed}
*reports can't be added when dossier is closed*

### Delete
Employee By username /employees{username}

Patient By ID /patients/{id} *will also delete dossier associated with the patient*

Report By ID /reports/{id}

