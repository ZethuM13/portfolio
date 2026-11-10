#!/usr/bin/env bash
set -euo pipefail

project_dir="$(cd "$(dirname "$0")/.." && pwd)"
cd "$project_dir"

./scripts/run.sh > /tmp/serviceplus-server.log 2>&1 &
server_pid=$!
trap 'kill "$server_pid" 2>/dev/null || true' EXIT

for _ in {1..20}; do
  if curl --fail --silent http://localhost:8080/api/health >/dev/null; then
    ./scripts/test.sh
    exit 0
  fi
  sleep 0.25
done

cat /tmp/serviceplus-server.log
exit 1
