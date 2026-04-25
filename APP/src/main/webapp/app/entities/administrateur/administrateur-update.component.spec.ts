import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import UtilisateurService from '@/entities/utilisateur/utilisateur.service';
import AlertService from '@/shared/alert/alert.service';

import AdministrateurUpdate from './administrateur-update.vue';
import AdministrateurService from './administrateur.service';

type AdministrateurUpdateComponentType = InstanceType<typeof AdministrateurUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const administrateurSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<AdministrateurUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Administrateur Management Update Component', () => {
    let comp: AdministrateurUpdateComponentType;
    let administrateurServiceStub: SinonStubbedInstance<AdministrateurService>;

    beforeEach(() => {
      route = {};
      administrateurServiceStub = sinon.createStubInstance<AdministrateurService>(AdministrateurService);
      administrateurServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          administrateurService: () => administrateurServiceStub,
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
        const wrapper = shallowMount(AdministrateurUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.administrateur = administrateurSample;
        administrateurServiceStub.update.resolves(administrateurSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(administrateurServiceStub.update.calledWith(administrateurSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        administrateurServiceStub.create.resolves(entity);
        const wrapper = shallowMount(AdministrateurUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.administrateur = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(administrateurServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        administrateurServiceStub.find.resolves(administrateurSample);
        administrateurServiceStub.retrieve.resolves([administrateurSample]);

        // WHEN
        route = {
          params: {
            administrateurId: `${administrateurSample.id}`,
          },
        };
        const wrapper = shallowMount(AdministrateurUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.administrateur).toMatchObject(administrateurSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        administrateurServiceStub.find.resolves(administrateurSample);
        const wrapper = shallowMount(AdministrateurUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
