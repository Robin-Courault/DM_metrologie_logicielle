import { beforeEach, describe, expect, it } from 'vitest';

import axios from 'axios';
import dayjs from 'dayjs';
import sinon from 'sinon';

import { DATE_TIME_FORMAT } from '@/shared/composables/date-format';
import { Offre } from '@/shared/model/offre.model';

import OffreService from './offre.service';

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
  describe('Offre Service', () => {
    let service: OffreService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new OffreService();
      currentDate = new Date();
      elemDefault = new Offre(123, currentDate, currentDate, 0);
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = {
          disponibleDe: dayjs(currentDate).format(DATE_TIME_FORMAT),
          disponibleJusqua: dayjs(currentDate).format(DATE_TIME_FORMAT),
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

      it('should create a Offre', async () => {
        const returnedFromService = {
          id: 123,
          disponibleDe: dayjs(currentDate).format(DATE_TIME_FORMAT),
          disponibleJusqua: dayjs(currentDate).format(DATE_TIME_FORMAT),
          ...elemDefault,
        };
        const expected = { disponibleDe: currentDate, disponibleJusqua: currentDate, ...returnedFromService };

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a Offre', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a Offre', async () => {
        const returnedFromService = {
          disponibleDe: dayjs(currentDate).format(DATE_TIME_FORMAT),
          disponibleJusqua: dayjs(currentDate).format(DATE_TIME_FORMAT),
          quantite: 1,
          ...elemDefault,
        };

        const expected = { disponibleDe: currentDate, disponibleJusqua: currentDate, ...returnedFromService };
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a Offre', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a Offre', async () => {
        const patchObject = {
          disponibleDe: dayjs(currentDate).format(DATE_TIME_FORMAT),
          disponibleJusqua: dayjs(currentDate).format(DATE_TIME_FORMAT),
          ...new Offre(),
        };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { disponibleDe: currentDate, disponibleJusqua: currentDate, ...returnedFromService };
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a Offre', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of Offre', async () => {
        const returnedFromService = {
          disponibleDe: dayjs(currentDate).format(DATE_TIME_FORMAT),
          disponibleJusqua: dayjs(currentDate).format(DATE_TIME_FORMAT),
          quantite: 1,
          ...elemDefault,
        };
        const expected = { disponibleDe: currentDate, disponibleJusqua: currentDate, ...returnedFromService };
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of Offre', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a Offre', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a Offre', async () => {
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
