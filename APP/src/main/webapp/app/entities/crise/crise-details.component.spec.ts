import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import CriseDetails from './crise-details.vue';
import CriseService from './crise.service';

type CriseDetailsComponentType = InstanceType<typeof CriseDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const criseSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Crise Management Detail Component', () => {
    let criseServiceStub: SinonStubbedInstance<CriseService>;
    let mountOptions: MountingOptions<CriseDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      criseServiceStub = sinon.createStubInstance<CriseService>(CriseService);

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
          criseService: () => criseServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        criseServiceStub.find.resolves(criseSample);
        route = {
          params: {
            criseId: `${123}`,
          },
        };
        const wrapper = shallowMount(CriseDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.crise).toMatchObject(criseSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        criseServiceStub.find.resolves(criseSample);
        const wrapper = shallowMount(CriseDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
