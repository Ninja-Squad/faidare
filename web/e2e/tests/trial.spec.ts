import { readFileSync } from 'node:fs';
import { join } from 'node:path';
import { expect, test } from '@playwright/test';

const basemap = readFileSync(join(__dirname, '../fixtures/natural-earth.svg'), 'utf8');

test.describe('Trial', () => {
  test.use({ viewport: { width: 1280, height: 1200 } });

  test('should display the trial page and export', async ({ page, context }) => {
    test.setTimeout(45_000);
    // Crop each requested tile from the same offline Web Mercator basemap.
    await page.route('https://server.arcgisonline.com/**/tile/**', route => {
      const [zoom, row, column] = new URL(route.request().url()).pathname.split('/').slice(-3).map(Number);
      const size = 256 / 2 ** zoom;
      return route.fulfill({
        contentType: 'image/svg+xml',
        body: basemap.replace('viewBox="0 0 256 256"', `viewBox="${column * size} ${row * size} ${size} ${size}"`)
      });
    });
    await page.goto('/faidare-dev/trials/dXJuOklOUkFFLVVSR0kvdHJpYWwvNDI=');

    await expect(page.getByRole('heading', { level: 1 })).toHaveText('Trial Network: Drops Phenotyping Network');

    const map = page.locator('#map-container');
    await expect(map.locator('.leaflet-tile-loaded').first()).toBeVisible();
    await expect(map.locator('.marker-cluster').first()).toBeVisible();
    await expect(map).toHaveScreenshot('trial-map.png');

    await map.locator('img.leaflet-marker-icon').first().click();
    await expect(map.locator('.leaflet-popup').getByRole('link', { name: 'Details' })).toBeVisible();
    await map.getByRole('button', { name: 'Close popup' }).click();

    await page.getByRole('button', { name: 'Export observations' }).click();
    const dropdown = page.locator('.dropdown-menu');
    await dropdown.getByRole('button', { name: 'Level: REPLICATE>BLOCK>PLOT', exact: true }).click();

    await expect(page.getByRole('heading', { level: 1, name: 'Export observations for trial Drops Phenotyping Network' })).toBeVisible();
    await expect(page.getByRole('dialog').locator('.modal-content')).toHaveScreenshot('trial-export-modal.png');

    // check all years
    await page.locator('#season-names').getByRole('button', { name: 'All' }).click();

    await expect(page.getByLabel('2011')).toBeChecked();
    await expect(page.getByLabel('2012')).toBeChecked();
    await expect(page.getByLabel('2013')).toBeChecked();

    // uncheck all years
    await page.locator('#season-names').getByRole('button', { name: 'None' }).click();
    await expect(page.getByLabel('2011')).not.toBeChecked();
    await expect(page.getByLabel('2012')).not.toBeChecked();
    await expect(page.getByLabel('2013')).not.toBeChecked();

    // check all study locations
    await page.locator('#study-locations').getByRole('button', { name: 'All' }).click();

    await expect(page.getByLabel('Bologna')).toBeChecked();
    await expect(page.getByLabel('Campagnola')).toBeChecked();

    // uncheck all study locations
    await page.locator('#study-locations').getByRole('button', { name: 'None' }).click();
    await expect(page.getByLabel('Bologna')).not.toBeChecked();
    await expect(page.getByLabel('Campagnola')).not.toBeChecked();

    // check all observation variable names
    await page.locator('#observation-variable-names').getByRole('button', { name: 'All' }).click();

    await expect(page.getByLabel('L10')).toBeChecked();
    await expect(page.getByLabel('L11')).toBeChecked();

    // uncheck all observation variable names
    await page.locator('#observation-variable-names').getByRole('button', { name: 'None' }).click();
    await expect(page.getByLabel('L10')).not.toBeChecked();
    await expect(page.getByLabel('L11')).not.toBeChecked();

    await expect(page.getByLabel('Excel')).toBeChecked();

    // fill the form
    await page.getByLabel('2013').check();
    await page.getByLabel('Gaillac').check();
    await page.getByLabel('Psi_Fill').check();
    await page.getByLabel('CSV').check();

    const exportPagePromise = context.waitForEvent('page');

    await page.getByRole('button', { name: 'Export', exact: true }).click();
    await expect(
      page.getByRole('heading', { level: 1, name: 'Export observations for trial Drops Phenotyping Network' })
    ).not.toBeVisible();

    const exportPage = await exportPagePromise;

    await expect(exportPage.getByRole('heading', { level: 1 })).toHaveText('Export for trial Network: Drops Phenotyping Network');
    await expect(exportPage.getByRole('alert')).toContainText('The export is in progress.');

    await expect(exportPage.getByRole('alert')).toContainText('The export succeeded.', { timeout: 30_000 });

    const downloadPromise = exportPage.waitForEvent('download');
    await exportPage.getByRole('link', { name: 'Download' }).click();

    const download = await downloadPromise;
    expect(download.suggestedFilename().endsWith('.csv')).toBe(true);
  });
});
