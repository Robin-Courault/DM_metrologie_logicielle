import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import UtilisateurService from '@/entities/utilisateur/utilisateur.service';
import AlertService from '@/shared/alert/alert.service';

import SinistreUpdate from './sinistre-update.vue';
import SinistreService from './sinistre.service';

type SinistreUpdateComponentType = InstanceType<typeof SinistreUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const sinistreSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<SinistreUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Sinistre Management Update Component', () => {
    let comp: SinistreUpdateComponentType;
    let sinistreServiceStub: SinonStubbedInstance<SinistreService>;

    beforeEach(() => {
      route = {};
      sinistreServiceStub = sinon.createStubInstance<SinistreService>(SinistreService);
      sinistreServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          sinistreService: () => sinistreServiceStub,
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
        const wrapper = shallowMount(SinistreUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.sinistre = sinistreSample;
        sinistreServiceStub.update.resolves(sinistreSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(sinistreServiceStub.update.calledWith(sinistreSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        sinistreServiceStub.create.resolves(entity);
        const wrapper = shallowMount(SinistreUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.sinistre = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(sinistreServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        sinistreServiceStub.find.resolves(sinistreSample);
        sinistreServiceStub.retrieve.resolves([sinistreSample]);

        // WHEN
        route = {
          params: {
            sinistreId: `${sinistreSample.id}`,
          },
        };
        const wrapper = shallowMount(SinistreUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.sinistre).toMatchObject(sinistreSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        sinistreServiceStub.find.resolves(sinistreSample);
        const wrapper = shallowMount(SinistreUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
