#!/usr/bin/env bash
set -euo pipefail

project_dir="$(cd "$(dirname "$0")/.." && pwd)"
cd "$project_dir"
exec java --source 17 --add-modules jdk.httpserver backend/src/com/serviceplus/Main.java
