#!/bin/sh
set -eu

cd "$(dirname "$0")/../.."
playwright_version=$(jq -er '.devDependencies["@playwright/test"]' package.json)
package_manager=$(jq -er '.packageManager' package.json)

host=host.docker.internal
[ "$(uname -s)" != Linux ] || host=localhost

rm -rf ./node_modules

status=0
docker run --rm --network host --platform linux/amd64 --user "$(id -u):$(id -g)" \
    -v "$(pwd):/work" -w /work --env HOME=/tmp --env "BASE_URL=${BASE_URL:-http://$host:8381}" \
    "mcr.microsoft.com/playwright:v${playwright_version}-noble" \
    npm exec --yes --package="$package_manager" -- sh -ec \
    'pnpm install --frozen-lockfile --store-dir /tmp/pnpm-store; pnpm e2e:headless --update-snapshots --retries=3' \
    || status=$?

rm -rf ./node_modules
pnpm install
exit "$status"
