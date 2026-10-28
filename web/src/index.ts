import { initializePopovers } from './bootstrap/popovers';
import { initializeMap } from './map/map';
import 'bootstrap/js/dist/dropdown';
import 'bootstrap/js/dist/collapse';
import 'bootstrap/js/dist/modal';
import { initializeTrialExport } from './trial-export/trial-export';
import { initializeTrial } from './trial/trial';

(window as any).faidare = {
  initializePopovers,
  initializeMap,
  initializeTrial,
  initializeTrialExport
};
