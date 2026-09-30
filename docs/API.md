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
