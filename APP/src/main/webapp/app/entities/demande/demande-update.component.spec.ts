import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import dayjs from 'dayjs';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AnnonceService from '@/entities/annonce/annonce.service';
import CriseService from '@/entities/crise/crise.service';
import OffreService from '@/entities/offre/offre.service';
import SalonDiscussionService from '@/entities/salon-discussion/salon-discussion.service';
import SinistreService from '@/entities/sinistre/sinistre.service';
import AlertService from '@/shared/alert/alert.service';
import { DATE_TIME_LONG_FORMAT } from '@/shared/composables/date-format';

import DemandeUpdate from './demande-update.vue';
import DemandeService from './demande.service';

type DemandeUpdateComponentType = InstanceType<typeof DemandeUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const demandeSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<DemandeUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Demande Management Update Component', () => {
    let comp: DemandeUpdateComponentType;
    let demandeServiceStub: SinonStubbedInstance<DemandeService>;

    beforeEach(() => {
      route = {};
      demandeServiceStub = sinon.createStubInstance<DemandeService>(DemandeService);
      demandeServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          demandeService: () => demandeServiceStub,
          annonceService: () =>
            sinon.createStubInstance<AnnonceService>(AnnonceService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          salonDiscussionService: () =>
            sinon.createStubInstance<SalonDiscussionService>(SalonDiscussionService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          sinistreService: () =>
            sinon.createStubInstance<SinistreService>(SinistreService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          criseService: () =>
            sinon.createStubInstance<CriseService>(CriseService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          offreService: () =>
            sinon.createStubInstance<OffreService>(OffreService, {
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
        const wrapper = shallowMount(DemandeUpdate, { global: mountOptions });
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
        const wrapper = shallowMount(DemandeUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.demande = demandeSample;
        demandeServiceStub.update.resolves(demandeSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(demandeServiceStub.update.calledWith(demandeSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        demandeServiceStub.create.resolves(entity);
        const wrapper = shallowMount(DemandeUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.demande = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(demandeServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        demandeServiceStub.find.resolves(demandeSample);
        demandeServiceStub.retrieve.resolves([demandeSample]);

        // WHEN
        route = {
          params: {
            demandeId: `${demandeSample.id}`,
          },
        };
        const wrapper = shallowMount(DemandeUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.demande).toMatchObject(demandeSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        demandeServiceStub.find.resolves(demandeSample);
        const wrapper = shallowMount(DemandeUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
