import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import OffreDetails from './offre-details.vue';
import OffreService from './offre.service';

type OffreDetailsComponentType = InstanceType<typeof OffreDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const offreSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Offre Management Detail Component', () => {
    let offreServiceStub: SinonStubbedInstance<OffreService>;
    let mountOptions: MountingOptions<OffreDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      offreServiceStub = sinon.createStubInstance<OffreService>(OffreService);

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
          offreService: () => offreServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        offreServiceStub.find.resolves(offreSample);
        route = {
          params: {
            offreId: `${123}`,
          },
        };
        const wrapper = shallowMount(OffreDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.offre).toMatchObject(offreSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        offreServiceStub.find.resolves(offreSample);
        const wrapper = shallowMount(OffreDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
