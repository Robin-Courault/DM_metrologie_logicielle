import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import SinistreDetails from './sinistre-details.vue';
import SinistreService from './sinistre.service';

type SinistreDetailsComponentType = InstanceType<typeof SinistreDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const sinistreSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Sinistre Management Detail Component', () => {
    let sinistreServiceStub: SinonStubbedInstance<SinistreService>;
    let mountOptions: MountingOptions<SinistreDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      sinistreServiceStub = sinon.createStubInstance<SinistreService>(SinistreService);

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
          sinistreService: () => sinistreServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        sinistreServiceStub.find.resolves(sinistreSample);
        route = {
          params: {
            sinistreId: `${123}`,
          },
        };
        const wrapper = shallowMount(SinistreDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.sinistre).toMatchObject(sinistreSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        sinistreServiceStub.find.resolves(sinistreSample);
        const wrapper = shallowMount(SinistreDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
