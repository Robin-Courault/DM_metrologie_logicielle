import { beforeEach, describe, expect, it } from 'vitest';

import axios from 'axios';
import dayjs from 'dayjs';
import sinon from 'sinon';

import { DATE_TIME_FORMAT } from '@/shared/composables/date-format';
import { Annonce } from '@/shared/model/annonce.model';

import AnnonceService from './annonce.service';

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
  describe('Annonce Service', () => {
    let service: AnnonceService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new AnnonceService();
      currentDate = new Date();
      elemDefault = new Annonce(123, 'AAAAAAA', 'AAAAAAA', 'PRET_MATERIEL', 0, 0, 'AAAAAAA', currentDate, currentDate, 'PUBLIEE');
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = {
          dateCreation: dayjs(currentDate).format(DATE_TIME_FORMAT),
          dateMaJ: dayjs(currentDate).format(DATE_TIME_FORMAT),
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

      it('should create a Annonce', async () => {
        const returnedFromService = {
          id: 123,
          dateCreation: dayjs(currentDate).format(DATE_TIME_FORMAT),
          dateMaJ: dayjs(currentDate).format(DATE_TIME_FORMAT),
          ...elemDefault,
        };
        const expected = { dateCreation: currentDate, dateMaJ: currentDate, ...returnedFromService };

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a Annonce', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a Annonce', async () => {
        const returnedFromService = {
          titre: 'BBBBBB',
          description: 'BBBBBB',
          categorie: 'BBBBBB',
          latitude: 1,
          longitude: 1,
          adresse: 'BBBBBB',
          dateCreation: dayjs(currentDate).format(DATE_TIME_FORMAT),
          dateMaJ: dayjs(currentDate).format(DATE_TIME_FORMAT),
          etat: 'BBBBBB',
          ...elemDefault,
        };

        const expected = { dateCreation: currentDate, dateMaJ: currentDate, ...returnedFromService };
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a Annonce', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a Annonce', async () => {
        const patchObject = { description: 'BBBBBB', latitude: 1, longitude: 1, etat: 'BBBBBB', ...new Annonce() };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { dateCreation: currentDate, dateMaJ: currentDate, ...returnedFromService };
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a Annonce', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of Annonce', async () => {
        const returnedFromService = {
          titre: 'BBBBBB',
          description: 'BBBBBB',
          categorie: 'BBBBBB',
          latitude: 1,
          longitude: 1,
          adresse: 'BBBBBB',
          dateCreation: dayjs(currentDate).format(DATE_TIME_FORMAT),
          dateMaJ: dayjs(currentDate).format(DATE_TIME_FORMAT),
          etat: 'BBBBBB',
          ...elemDefault,
        };
        const expected = { dateCreation: currentDate, dateMaJ: currentDate, ...returnedFromService };
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of Annonce', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a Annonce', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a Annonce', async () => {
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
