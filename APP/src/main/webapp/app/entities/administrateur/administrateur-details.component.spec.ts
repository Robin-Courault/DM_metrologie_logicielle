import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import AdministrateurDetails from './administrateur-details.vue';
import AdministrateurService from './administrateur.service';

type AdministrateurDetailsComponentType = InstanceType<typeof AdministrateurDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const administrateurSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Administrateur Management Detail Component', () => {
    let administrateurServiceStub: SinonStubbedInstance<AdministrateurService>;
    let mountOptions: MountingOptions<AdministrateurDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      administrateurServiceStub = sinon.createStubInstance<AdministrateurService>(AdministrateurService);

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
          administrateurService: () => administrateurServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        administrateurServiceStub.find.resolves(administrateurSample);
        route = {
          params: {
            administrateurId: `${123}`,
          },
        };
        const wrapper = shallowMount(AdministrateurDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.administrateur).toMatchObject(administrateurSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        administrateurServiceStub.find.resolves(administrateurSample);
        const wrapper = shallowMount(AdministrateurDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
