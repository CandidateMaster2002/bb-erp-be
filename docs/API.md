# BoltBlazers ERP Backend API

All endpoints except `/api/health` and `/api/auth/login` require authentication (e.g. Basic Auth per initial setup). Dates are in UTC (ISO-8601).

## Leads

### Create Lead (Quick Add)
```bash
curl -X POST http://localhost:8080/api/leads \
  -H "Content-Type: application/json" \
  -H "Authorization: Basic YWRtaW5AYm9sdGJsYXplcnMuY29tOmFkbWlu" \
  -d '{"fullName":"John Doe", "phone":"+1234567890", "note":"Met at conference"}'
```

### List Leads (Paginated & Filtered)
```bash
curl -X GET "http://localhost:8080/api/leads?page=0&size=20&q=John&priority=HOT" \
  -H "Authorization: Basic YWRtaW5AYm9sdGJsYXplcnMuY29tOmFkbWlu"
```

### Get Lead Profile
```bash
curl -X GET http://localhost:8080/api/leads/1 \
  -H "Authorization: Basic YWRtaW5AYm9sdGJsYXplcnMuY29tOmFkbWlu"
```

### Delete Lead
```bash
curl -X DELETE http://localhost:8080/api/leads/1 \
  -H "Authorization: Basic YWRtaW5AYm9sdGJsYXplcnMuY29tOmFkbWlu"
```

## Interactions

### Log Interaction
```bash
curl -X POST http://localhost:8080/api/leads/1/interactions \
  -H "Content-Type: application/json" \
  -H "Authorization: Basic YWRtaW5AYm9sdGJsYXplcnMuY29tOmFkbWlu" \
  -d '{"type":"CALL", "outcome":"PICKED_UP", "summary":"Discussed pricing"}'
```

### Get Lead Interactions
```bash
curl -X GET http://localhost:8080/api/leads/1/interactions \
  -H "Authorization: Basic YWRtaW5AYm9sdGJsYXplcnMuY29tOmFkbWlu"
```

## Follow-ups

### Create Follow-up
```bash
curl -X POST http://localhost:8080/api/leads/1/followups \
  -H "Content-Type: application/json" \
  -H "Authorization: Basic YWRtaW5AYm9sdGJsYXplcnMuY29tOmFkbWlu" \
  -d '{"dueAt":"2026-10-01T10:00:00Z", "note":"Call to confirm"}'
```

### Mark Follow-up Done
```bash
curl -X PATCH http://localhost:8080/api/followups/1/done \
  -H "Authorization: Basic YWRtaW5AYm9sdGJsYXplcnMuY29tOmFkbWlu"
```

### Snooze Follow-up
```bash
curl -X PATCH http://localhost:8080/api/followups/1/snooze \
  -H "Content-Type: application/json" \
  -H "Authorization: Basic YWRtaW5AYm9sdGJsYXplcnMuY29tOmFkbWlu" \
  -d '{"dueAt":"2026-10-02T10:00:00Z"}'
```

### List Today's Follow-ups
```bash
curl -X GET http://localhost:8080/api/followups/today \
  -H "Authorization: Basic YWRtaW5AYm9sdGJsYXplcnMuY29tOmFkbWlu"
```

## Commitments

### Create Commitment
```bash
curl -X POST http://localhost:8080/api/leads/1/commitments \
  -H "Content-Type: application/json" \
  -H "Authorization: Basic YWRtaW5AYm9sdGJsYXplcnMuY29tOmFkbWlu" \
  -d '{"item":"Send Brochure", "dueDate":"2026-10-05"}'
```

### Mark Commitment Sent
```bash
curl -X PATCH http://localhost:8080/api/commitments/1/sent \
  -H "Authorization: Basic YWRtaW5AYm9sdGJsYXplcnMuY29tOmFkbWlu"
```

### Get Pending Commitments
```bash
curl -X GET http://localhost:8080/api/commitments/pending \
  -H "Authorization: Basic YWRtaW5AYm9sdGJsYXplcnMuY29tOmFkbWlu"
```

## Dashboard
```bash
curl -X GET http://localhost:8080/api/dashboard \
  -H "Authorization: Basic YWRtaW5AYm9sdGJsYXplcnMuY29tOmFkbWlu"
```

## Imports

### Upload Leads CSV/Excel
```bash
curl -X POST http://localhost:8080/api/imports \
  -H "Authorization: Basic YWRtaW5AYm9sdGJsYXplcnMuY29tOmFkbWlu" \
  -F "file=@leads.csv"
```

## Reports (Timezone: Asia/Kolkata)
```bash
curl -X GET "http://localhost:8080/api/reports/leads-by-stage?dateFrom=2026-09-01T00:00:00Z&dateTo=2026-09-30T23:59:59Z" -H "Authorization: Basic YWRtaW5AYm9sdGJsYXplcnMuY29tOmFkbWlu"
curl -X GET http://localhost:8080/api/reports/leads-by-source -H "Authorization: Basic YWRtaW5AYm9sdGJsYXplcnMuY29tOmFkbWlu"
curl -X GET http://localhost:8080/api/reports/leads-by-category -H "Authorization: Basic YWRtaW5AYm9sdGJsYXplcnMuY29tOmFkbWlu"
curl -X GET http://localhost:8080/api/reports/conversion -H "Authorization: Basic YWRtaW5AYm9sdGJsYXplcnMuY29tOmFkbWlu"
curl -X GET http://localhost:8080/api/reports/avg-days-to-close -H "Authorization: Basic YWRtaW5AYm9sdGJsYXplcnMuY29tOmFkbWlu"
curl -X GET http://localhost:8080/api/reports/activity -H "Authorization: Basic YWRtaW5AYm9sdGJsYXplcnMuY29tOmFkbWlu"
curl -X GET http://localhost:8080/api/reports/followups -H "Authorization: Basic YWRtaW5AYm9sdGJsYXplcnMuY29tOmFkbWlu"
```

## Saved Filters
```bash
curl -X POST http://localhost:8080/api/saved-filters \
  -H "Content-Type: application/json" \
  -H "Authorization: Basic YWRtaW5AYm9sdGJsYXplcnMuY29tOmFkbWlu" \
  -d '{"name":"Hot Leads in Mumbai", "filterJson":"{\"priority\":\"HOT\", \"city\":\"Mumbai\"}"}'

curl -X GET http://localhost:8080/api/saved-filters -H "Authorization: Basic YWRtaW5AYm9sdGJsYXplcnMuY29tOmFkbWlu"
curl -X DELETE http://localhost:8080/api/saved-filters/1 -H "Authorization: Basic YWRtaW5AYm9sdGJsYXplcnMuY29tOmFkbWlu"
```

## Reminders
```bash
curl -X GET http://localhost:8080/api/reminders/summary -H "Authorization: Basic YWRtaW5AYm9sdGJsYXplcnMuY29tOmFkbWlu"
curl -X GET http://localhost:8080/api/reminders/due-now -H "Authorization: Basic YWRtaW5AYm9sdGJsYXplcnMuY29tOmFkbWlu"
curl -X PATCH http://localhost:8080/api/followups/1/acknowledge -H "Authorization: Basic YWRtaW5AYm9sdGJsYXplcnMuY29tOmFkbWlu"
```
