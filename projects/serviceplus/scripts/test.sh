#!/usr/bin/env bash
set -euo pipefail

base_url="${SERVICEPLUS_URL:-http://localhost:8080}"

health="$(curl --fail --silent "$base_url/api/health")"
[[ "$health" == *'"status":"UP"'* ]]

services="$(curl --fail --silent "$base_url/api/services")"
[[ "$services" == *'Customer Identity Gateway'* ]]

created="$(curl --fail --silent -X POST "$base_url/api/services" \
  -H 'Content-Type: application/json' \
  -d '{"name":"Notification Gateway","owner":"Zethu Mkhize","domain":"Customer Experience","criticality":"HIGH","status":"HEALTHY"}')"
[[ "$created" == *'Notification Gateway'* ]]

status="$(curl --silent --output /dev/null --write-out '%{http_code}' -X POST "$base_url/api/services" \
  -H 'Content-Type: application/json' -d '{"name":"Incomplete"}')"
[[ "$status" == "400" ]]

printf 'ServicePlus smoke tests passed.\n'
