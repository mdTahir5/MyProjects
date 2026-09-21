#!/usr/bin/env bash
# ------------------------------------------------------------------
# Loads backend/.env into the current shell session.
# Usage:  source scripts/set-env.sh
# ------------------------------------------------------------------
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ENV_FILE="${SCRIPT_DIR}/../.env"

if [[ ! -f "${ENV_FILE}" ]]; then
    echo "No .env found at ${ENV_FILE} - falling back to .env.example"
    ENV_FILE="${SCRIPT_DIR}/../.env.example"
fi

set -a
# shellcheck disable=SC1090
source "${ENV_FILE}"
set +a

echo "Environment loaded from ${ENV_FILE}"
