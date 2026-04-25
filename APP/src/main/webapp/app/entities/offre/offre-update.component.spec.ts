import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import dayjs from 'dayjs';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AnnonceService from '@/entities/annonce/annonce.service';
import CitoyenService from '@/entities/citoyen/citoyen.service';
import CriseService from '@/entities/crise/crise.service';
import DemandeService from '@/entities/demande/demande.service';
import AlertService from '@/shared/alert/alert.service';
import { DATE_TIME_LONG_FORMAT } from '@/shared/composables/date-format';

import OffreUpdate from './offre-update.vue';
import OffreService from './offre.service';

type OffreUpdateComponentType = InstanceType<typeof OffreUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const offreSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<OffreUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Offre Management Update Component', () => {
    let comp: OffreUpdateComponentType;
    let offreServiceStub: SinonStubbedInstance<OffreService>;

    beforeEach(() => {
      route = {};
      offreServiceStub = sinon.createStubInstance<OffreService>(OffreService);
      offreServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        toast: {
          show: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'b-input-group': true,
          'b-input-group-prepend': true,
          'b-form-datepicker': true,
          'b-form-input': true,
        },
        provide: {
          alertService,
          offreService: () => offreServiceStub,
          annonceService: () =>
            sinon.createStubInstance<AnnonceService>(AnnonceService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          citoyenService: () =>
            sinon.createStubInstance<CitoyenService>(CitoyenService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          criseService: () =>
            sinon.createStubInstance<CriseService>(CriseService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          demandeService: () =>
            sinon.createStubInstance<DemandeService>(DemandeService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('load', () => {
      beforeEach(() => {
        const wrapper = shallowMount(OffreUpdate, { global: mountOptions });
        comp = wrapper.vm;
      });
      it('Should convert date from string', () => {
        // GIVEN
        const date = new Date('2019-10-15T11:42:02Z');

        // WHEN
        const convertedDate = comp.convertDateTimeFromServer(date);

        // THEN
        expect(convertedDate).toEqual(dayjs(date).format(DATE_TIME_LONG_FORMAT));
      });

      it('Should not convert date if date is not present', () => {
        expect(comp.convertDateTimeFromServer(null)).toBeNull();
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(OffreUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.offre = offreSample;
        offreServiceStub.update.resolves(offreSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(offreServiceStub.update.calledWith(offreSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        offreServiceStub.create.resolves(entity);
        const wrapper = shallowMount(OffreUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.offre = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(offreServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        offreServiceStub.find.resolves(offreSample);
        offreServiceStub.retrieve.resolves([offreSample]);

        // WHEN
        route = {
          params: {
            offreId: `${offreSample.id}`,
          },
        };
        const wrapper = shallowMount(OffreUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.offre).toMatchObject(offreSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        offreServiceStub.find.resolves(offreSample);
        const wrapper = shallowMount(OffreUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
