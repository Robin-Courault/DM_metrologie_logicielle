import { beforeEach, describe, expect, it } from 'vitest';

import axios from 'axios';
import dayjs from 'dayjs';
import sinon from 'sinon';

import { DATE_TIME_FORMAT } from '@/shared/composables/date-format';
import { Demande } from '@/shared/model/demande.model';

import DemandeService from './demande.service';

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
  describe('Demande Service', () => {
    let service: DemandeService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new DemandeService();
      currentDate = new Date();
      elemDefault = new Demande(123, 'OUVERTE', currentDate, 0);
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = { dateFermeture: dayjs(currentDate).format(DATE_TIME_FORMAT), ...elemDefault };
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

      it('should create a Demande', async () => {
        const returnedFromService = { id: 123, dateFermeture: dayjs(currentDate).format(DATE_TIME_FORMAT), ...elemDefault };
        const expected = { dateFermeture: currentDate, ...returnedFromService };

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a Demande', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a Demande', async () => {
        const returnedFromService = {
          etatDemande: 'BBBBBB',
          dateFermeture: dayjs(currentDate).format(DATE_TIME_FORMAT),
          quantite: 1,
          ...elemDefault,
        };

        const expected = { dateFermeture: currentDate, ...returnedFromService };
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a Demande', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a Demande', async () => {
        const patchObject = { etatDemande: 'BBBBBB', dateFermeture: dayjs(currentDate).format(DATE_TIME_FORMAT), ...new Demande() };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { dateFermeture: currentDate, ...returnedFromService };
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a Demande', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of Demande', async () => {
        const returnedFromService = {
          etatDemande: 'BBBBBB',
          dateFermeture: dayjs(currentDate).format(DATE_TIME_FORMAT),
          quantite: 1,
          ...elemDefault,
        };
        const expected = { dateFermeture: currentDate, ...returnedFromService };
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of Demande', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a Demande', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a Demande', async () => {
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
