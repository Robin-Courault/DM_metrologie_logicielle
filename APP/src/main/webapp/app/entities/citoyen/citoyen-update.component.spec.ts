import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import UtilisateurService from '@/entities/utilisateur/utilisateur.service';
import AlertService from '@/shared/alert/alert.service';

import CitoyenUpdate from './citoyen-update.vue';
import CitoyenService from './citoyen.service';

type CitoyenUpdateComponentType = InstanceType<typeof CitoyenUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const citoyenSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<CitoyenUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Citoyen Management Update Component', () => {
    let comp: CitoyenUpdateComponentType;
    let citoyenServiceStub: SinonStubbedInstance<CitoyenService>;

    beforeEach(() => {
      route = {};
      citoyenServiceStub = sinon.createStubInstance<CitoyenService>(CitoyenService);
      citoyenServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          citoyenService: () => citoyenServiceStub,
          utilisateurService: () =>
            sinon.createStubInstance<UtilisateurService>(UtilisateurService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(CitoyenUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.citoyen = citoyenSample;
        citoyenServiceStub.update.resolves(citoyenSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(citoyenServiceStub.update.calledWith(citoyenSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        citoyenServiceStub.create.resolves(entity);
        const wrapper = shallowMount(CitoyenUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.citoyen = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(citoyenServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        citoyenServiceStub.find.resolves(citoyenSample);
        citoyenServiceStub.retrieve.resolves([citoyenSample]);

        // WHEN
        route = {
          params: {
            citoyenId: `${citoyenSample.id}`,
          },
        };
        const wrapper = shallowMount(CitoyenUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.citoyen).toMatchObject(citoyenSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        citoyenServiceStub.find.resolves(citoyenSample);
        const wrapper = shallowMount(CitoyenUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
