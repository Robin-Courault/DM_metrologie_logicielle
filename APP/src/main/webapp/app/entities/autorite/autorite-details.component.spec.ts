import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import AutoriteDetails from './autorite-details.vue';
import AutoriteService from './autorite.service';

type AutoriteDetailsComponentType = InstanceType<typeof AutoriteDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const autoriteSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Autorite Management Detail Component', () => {
    let autoriteServiceStub: SinonStubbedInstance<AutoriteService>;
    let mountOptions: MountingOptions<AutoriteDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      autoriteServiceStub = sinon.createStubInstance<AutoriteService>(AutoriteService);

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        toast: {
          show: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'router-link': true,
        },
        provide: {
          alertService,
          autoriteService: () => autoriteServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        autoriteServiceStub.find.resolves(autoriteSample);
        route = {
          params: {
            autoriteId: `${123}`,
          },
        };
        const wrapper = shallowMount(AutoriteDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.autorite).toMatchObject(autoriteSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        autoriteServiceStub.find.resolves(autoriteSample);
        const wrapper = shallowMount(AutoriteDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
