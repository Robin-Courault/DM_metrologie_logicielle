import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import AnnonceDetails from './annonce-details.vue';
import AnnonceService from './annonce.service';

type AnnonceDetailsComponentType = InstanceType<typeof AnnonceDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const annonceSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Annonce Management Detail Component', () => {
    let annonceServiceStub: SinonStubbedInstance<AnnonceService>;
    let mountOptions: MountingOptions<AnnonceDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      annonceServiceStub = sinon.createStubInstance<AnnonceService>(AnnonceService);

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
          annonceService: () => annonceServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        annonceServiceStub.find.resolves(annonceSample);
        route = {
          params: {
            annonceId: `${123}`,
          },
        };
        const wrapper = shallowMount(AnnonceDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.annonce).toMatchObject(annonceSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        annonceServiceStub.find.resolves(annonceSample);
        const wrapper = shallowMount(AnnonceDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
