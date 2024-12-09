import dayjs from 'dayjs/esm';

import { IAuditLog, NewAuditLog } from './audit-log.model';

export const sampleWithRequiredData: IAuditLog = {
  id: 22622,
  uuid: '4a82c325-e257-484d-ae97-e5ec673c63fe',
  entityName: 'jealously',
  entityId: '2a139707-088e-4e0c-8483-4faae9b8917c',
  action: 'with',
  performedDate: dayjs('2024-12-09T11:53'),
};

export const sampleWithPartialData: IAuditLog = {
  id: 8627,
  uuid: '68433580-0b2d-4b6a-a2bc-f29f54fc3c28',
  entityName: 'fiddle knowingly',
  entityId: '958bca40-b0ae-4fab-a540-4e5ba3043b06',
  action: 'eek bob',
  performedBy: 'till agreeable',
  performedDate: dayjs('2024-12-08T23:58'),
};

export const sampleWithFullData: IAuditLog = {
  id: 17537,
  uuid: '568b0738-88ee-4051-bee6-fba0217982d6',
  entityName: 'smoothly',
  entityId: '8579399b-bc3f-4942-ad93-b96d81374095',
  action: 'trustworthy',
  performedBy: 'deck incidentally',
  performedDate: dayjs('2024-12-09T01:32'),
  details: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewAuditLog = {
  uuid: '20614b83-5d4f-4b91-8e1e-742af6249797',
  entityName: 'breastplate glider',
  entityId: '0d074ed5-35c4-47fc-865f-8d49a5dc330e',
  action: 'the',
  performedDate: dayjs('2024-12-08T23:51'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
