# Seed data guide (use your API save endpoints)

The examination requires seed data entered through your own REST endpoints. Use these suggested records and capture UUIDs from each response before creating linked records.

1. `POST /api/incident-types/save`: Theft, Suspicious Activity, Fire, Medical Emergency.
2. `POST /api/patrol-zones/save`: zoneNumber 1, 2, 3, 4. Keep zone 4 unassigned.
3. `POST /api/security-guards/save`: create 3 guards assigned to zones 1-3. Give one guard two incident types and another exactly one. Add types by sending their UUIDs in the `incidentTypes` array or using `PUT /api/security-guards/{guardId}/incident-types/{typeId}`.
4. `POST /api/residents/save`: create 5 residents; make two have `street: "KG 11 Ave"`. Use unique nationalId values.
5. `POST /api/incident-reports/save`: create 10 reports across October and November 2026 using all statuses through the status PATCH endpoint. One resident needs at least 3 reports; the same guard needs two reports dated 2026-10-20, with one dismissed; at least two reports must be RESOLVED.
6. `POST /api/response-actions/save?incidentReportId={resolvedReportId}`: create response actions only for resolved reports.

Important: the save rule rejects dates before today's date. Because the examination paper specifies October/November 2026 sample dates and today's date may be after some of those dates when you test, create the records before their report date passes or adjust test dates to today/future while demonstrating the business rule. Do not change the required implementation logic.
