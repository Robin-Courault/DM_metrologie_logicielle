import { beforeEach, describe, expect, it } from 'vitest';

import axios from 'axios';
import dayjs from 'dayjs';
import sinon from 'sinon';

import { DATE_TIME_FORMAT } from '@/shared/composables/date-format';
import { Utilisateur } from '@/shared/model/utilisateur.model';

import UtilisateurService from './utilisateur.service';

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
  describe('Utilisateur Service', () => {
    let service: UtilisateurService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new UtilisateurService();
      currentDate = new Date();
      elemDefault = new Utilisateur(123, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', currentDate, false, false);
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = { dateInscription: dayjs(currentDate).format(DATE_TIME_FORMAT), ...elemDefault };
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

      it('should create a Utilisateur', async () => {
        const returnedFromService = { id: 123, dateInscription: dayjs(currentDate).format(DATE_TIME_FORMAT), ...elemDefault };
        const expected = { dateInscription: currentDate, ...returnedFromService };

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a Utilisateur', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a Utilisateur', async () => {
        const returnedFromService = {
          login: 'BBBBBB',
          nom: 'BBBBBB',
          prenom: 'BBBBBB',
          email: 'BBBBBB',
          telephone: 'BBBBBB',
          motDePasse: 'BBBBBB',
          dateInscription: dayjs(currentDate).format(DATE_TIME_FORMAT),
          actif: true,
          banni: true,
          ...elemDefault,
        };

        const expected = { dateInscription: currentDate, ...returnedFromService };
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a Utilisateur', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a Utilisateur', async () => {
        const patchObject = { nom: 'BBBBBB', email: 'BBBBBB', banni: true, ...new Utilisateur() };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { dateInscription: currentDate, ...returnedFromService };
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a Utilisateur', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of Utilisateur', async () => {
        const returnedFromService = {
          login: 'BBBBBB',
          nom: 'BBBBBB',
          prenom: 'BBBBBB',
          email: 'BBBBBB',
          telephone: 'BBBBBB',
          motDePasse: 'BBBBBB',
          dateInscription: dayjs(currentDate).format(DATE_TIME_FORMAT),
          actif: true,
          banni: true,
          ...elemDefault,
        };
        const expected = { dateInscription: currentDate, ...returnedFromService };
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve().then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of Utilisateur', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a Utilisateur', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a Utilisateur', async () => {
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
