import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import AutoriteUpdate from './autorite-update.vue';
import AutoriteService from './autorite.service';

type AutoriteUpdateComponentType = InstanceType<typeof AutoriteUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const autoriteSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<AutoriteUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Autorite Management Update Component', () => {
    let comp: AutoriteUpdateComponentType;
    let autoriteServiceStub: SinonStubbedInstance<AutoriteService>;

    beforeEach(() => {
      route = {};
      autoriteServiceStub = sinon.createStubInstance<AutoriteService>(AutoriteService);
      autoriteServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          autoriteService: () => autoriteServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(AutoriteUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.autorite = autoriteSample;
        autoriteServiceStub.update.resolves(autoriteSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(autoriteServiceStub.update.calledWith(autoriteSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        autoriteServiceStub.create.resolves(entity);
        const wrapper = shallowMount(AutoriteUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.autorite = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(autoriteServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        autoriteServiceStub.find.resolves(autoriteSample);
        autoriteServiceStub.retrieve.resolves([autoriteSample]);

        // WHEN
        route = {
          params: {
            autoriteId: `${autoriteSample.id}`,
          },
        };
        const wrapper = shallowMount(AutoriteUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.autorite).toMatchObject(autoriteSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        autoriteServiceStub.find.resolves(autoriteSample);
        const wrapper = shallowMount(AutoriteUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
