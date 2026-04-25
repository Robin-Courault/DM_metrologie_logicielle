import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import UtilisateurDetails from './utilisateur-details.vue';
import UtilisateurService from './utilisateur.service';

type UtilisateurDetailsComponentType = InstanceType<typeof UtilisateurDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const utilisateurSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Utilisateur Management Detail Component', () => {
    let utilisateurServiceStub: SinonStubbedInstance<UtilisateurService>;
    let mountOptions: MountingOptions<UtilisateurDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      utilisateurServiceStub = sinon.createStubInstance<UtilisateurService>(UtilisateurService);

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
          utilisateurService: () => utilisateurServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        utilisateurServiceStub.find.resolves(utilisateurSample);
        route = {
          params: {
            utilisateurId: `${123}`,
          },
        };
        const wrapper = shallowMount(UtilisateurDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.utilisateur).toMatchObject(utilisateurSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        utilisateurServiceStub.find.resolves(utilisateurSample);
        const wrapper = shallowMount(UtilisateurDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
