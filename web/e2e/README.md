# End-to-end tests

This project contains the end-to-end tests for the project.
It uses [Playwright](https://playwright.dev) to run the tests on the running application.

## Launch tests during development

Launch the backend application with the `dev` and `e2e` Spring profiles, and then run:

    pnpm e2e

## Trial screenshot baselines

Playwright keeps separate macOS and Linux baselines for the trial map and export
modal. The map uses offline tiles from [Natural Earth](https://github.com/nvkelso/natural-earth-vector/blob/v5.1.2/geojson/ne_110m_land.geojson).

Start the app on port 8381 with the `e2e` profile and seed Elasticsearch with
`data/test`. From `web/`, regenerate the macOS baselines on a Mac:

    pnpm e2e:headless trial.spec.ts --update-snapshots

Regenerate the Linux CI baselines with Docker and `jq`. On macOS, enable host
networking in Docker Desktop first:

    ./e2e/scripts/regen-e2e-snapshots-for-ci.sh

The script replaces and reinstalls `web/node_modules`. Set `BASE_URL` if Docker
needs a different URL to reach the app.
