import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import DemandeDetails from './demande-details.vue';
import DemandeService from './demande.service';

type DemandeDetailsComponentType = InstanceType<typeof DemandeDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const demandeSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Demande Management Detail Component', () => {
    let demandeServiceStub: SinonStubbedInstance<DemandeService>;
    let mountOptions: MountingOptions<DemandeDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      demandeServiceStub = sinon.createStubInstance<DemandeService>(DemandeService);

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
          demandeService: () => demandeServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        demandeServiceStub.find.resolves(demandeSample);
        route = {
          params: {
            demandeId: `${123}`,
          },
        };
        const wrapper = shallowMount(DemandeDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.demande).toMatchObject(demandeSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        demandeServiceStub.find.resolves(demandeSample);
        const wrapper = shallowMount(DemandeDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
