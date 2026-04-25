import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import AgentDetails from './agent-details.vue';
import AgentService from './agent.service';

type AgentDetailsComponentType = InstanceType<typeof AgentDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const agentSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Agent Management Detail Component', () => {
    let agentServiceStub: SinonStubbedInstance<AgentService>;
    let mountOptions: MountingOptions<AgentDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      agentServiceStub = sinon.createStubInstance<AgentService>(AgentService);

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
          agentService: () => agentServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        agentServiceStub.find.resolves(agentSample);
        route = {
          params: {
            agentId: `${123}`,
          },
        };
        const wrapper = shallowMount(AgentDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.agent).toMatchObject(agentSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        agentServiceStub.find.resolves(agentSample);
        const wrapper = shallowMount(AgentDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
