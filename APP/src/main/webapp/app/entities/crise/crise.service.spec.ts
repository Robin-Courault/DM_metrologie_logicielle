import { beforeEach, describe, expect, it } from 'vitest';

import axios from 'axios';
import dayjs from 'dayjs';
import sinon from 'sinon';

import { DATE_TIME_FORMAT } from '@/shared/composables/date-format';
import { Crise } from '@/shared/model/crise.model';

import CriseService from './crise.service';

const error = {
  response: {
    status: null,
    data: {
      type: null,
    },
  },
};

const axiosStub = {
  get: sinon.stub(axios, 'get'),
  post: sinon.stub(axios, 'post'),
  put: sinon.stub(axios, 'put'),
  patch: sinon.stub(axios, 'patch'),
  delete: sinon.stub(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('Crise Service', () => {
    let service: CriseService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new CriseService();
      currentDate = new Date();
      elemDefault = new Crise(123, 'AAAAAAA', 'AAAAAAA', 'INCENDIE', currentDate, currentDate, 'AAAAAAA', false);
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = {
          dateDebut: dayjs(currentDate).format(DATE_TIME_FORMAT),
          dateFin: dayjs(currentDate).format(DATE_TIME_FORMAT),
          ...elemDefault,
        };
        axiosStub.get.resolves({ data: returnedFromService });

        return service.find(123).then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.rejects(error);
        return service
          .find(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a Crise', async () => {
        const returnedFromService = {
          id: 123,
          dateDebut: dayjs(currentDate).format(DATE_TIME_FORMAT),
          dateFin: dayjs(currentDate).format(DATE_TIME_FORMAT),
          ...elemDefault,
        };
        const expected = { dateDebut: currentDate, dateFin: currentDate, ...returnedFromService };

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a Crise', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a Crise', async () => {
        const returnedFromService = {
          titre: 'BBBBBB',
          description: 'BBBBBB',
          type: 'BBBBBB',
          dateDebut: dayjs(currentDate).format(DATE_TIME_FORMAT),
          dateFin: dayjs(currentDate).format(DATE_TIME_FORMAT),
          zoneGeographique: 'BBBBBB',
          cloturee: true,
          ...elemDefault,
        };

        const expected = { dateDebut: currentDate, dateFin: currentDate, ...returnedFromService };
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a Crise', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a Crise', async () => {
        const patchObject = {
          description: 'BBBBBB',
          type: 'BBBBBB',
          dateDebut: dayjs(currentDate).format(DATE_TIME_FORMAT),
          cloturee: true,
          ...new Crise(),
        };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { dateDebut: currentDate, dateFin: currentDate, ...returnedFromService };
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a Crise', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of Crise', async () => {
        const returnedFromService = {
          titre: 'BBBBBB',
          description: 'BBBBBB',
          type: 'BBBBBB',
          dateDebut: dayjs(currentDate).format(DATE_TIME_FORMAT),
          dateFin: dayjs(currentDate).format(DATE_TIME_FORMAT),
          zoneGeographique: 'BBBBBB',
          cloturee: true,
          ...elemDefault,
        };
        const expected = { dateDebut: currentDate, dateFin: currentDate, ...returnedFromService };
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of Crise', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a Crise', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a Crise', async () => {
        axiosStub.delete.rejects(error);

        return service
          .delete(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
