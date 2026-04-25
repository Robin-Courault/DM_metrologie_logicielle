import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import CitoyenDetails from './citoyen-details.vue';
import CitoyenService from './citoyen.service';

type CitoyenDetailsComponentType = InstanceType<typeof CitoyenDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const citoyenSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Citoyen Management Detail Component', () => {
    let citoyenServiceStub: SinonStubbedInstance<CitoyenService>;
    let mountOptions: MountingOptions<CitoyenDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      citoyenServiceStub = sinon.createStubInstance<CitoyenService>(CitoyenService);

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
          citoyenService: () => citoyenServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        citoyenServiceStub.find.resolves(citoyenSample);
        route = {
          params: {
            citoyenId: `${123}`,
          },
        };
        const wrapper = shallowMount(CitoyenDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.citoyen).toMatchObject(citoyenSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        citoyenServiceStub.find.resolves(citoyenSample);
        const wrapper = shallowMount(CitoyenDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
