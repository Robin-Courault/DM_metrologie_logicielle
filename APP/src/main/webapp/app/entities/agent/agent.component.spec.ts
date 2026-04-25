import { beforeEach, describe, expect, it, vitest } from 'vitest';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import AgentService from './agent.service';
import Agent from './agent.vue';

type AgentComponentType = InstanceType<typeof Agent>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('Agent Management Component', () => {
    let agentServiceStub: SinonStubbedInstance<AgentService>;
    let mountOptions: MountingOptions<AgentComponentType>['global'];

    beforeEach(() => {
      agentServiceStub = sinon.createStubInstance<AgentService>(AgentService);
      agentServiceStub.retrieve.resolves({ headers: {} });

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        toast: {
          show: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          bModal: bModalStub as any,
          'font-awesome-icon': true,
          'b-badge': true,
          'b-button': true,
          'router-link': true,
        },
        directives: {
          'b-modal': {},
        },
        provide: {
          alertService,
          agentService: () => agentServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        agentServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(Agent, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(agentServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.agents[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: AgentComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(Agent, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        agentServiceStub.retrieve.reset();
        agentServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        agentServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeAgent();
        await comp.$nextTick(); // clear components

        // THEN
        expect(agentServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(agentServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
