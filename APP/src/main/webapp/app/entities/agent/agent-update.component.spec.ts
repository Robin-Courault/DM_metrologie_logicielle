import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AutoriteService from '@/entities/autorite/autorite.service';
import UtilisateurService from '@/entities/utilisateur/utilisateur.service';
import AlertService from '@/shared/alert/alert.service';

import AgentUpdate from './agent-update.vue';
import AgentService from './agent.service';

type AgentUpdateComponentType = InstanceType<typeof AgentUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const agentSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<AgentUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Agent Management Update Component', () => {
    let comp: AgentUpdateComponentType;
    let agentServiceStub: SinonStubbedInstance<AgentService>;

    beforeEach(() => {
      route = {};
      agentServiceStub = sinon.createStubInstance<AgentService>(AgentService);
      agentServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        toast: {
          show: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'b-input-group': true,
          'b-input-group-prepend': true,
          'b-form-datepicker': true,
          'b-form-input': true,
        },
        provide: {
          alertService,
          agentService: () => agentServiceStub,
          utilisateurService: () =>
            sinon.createStubInstance<UtilisateurService>(UtilisateurService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          autoriteService: () =>
            sinon.createStubInstance<AutoriteService>(AutoriteService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(AgentUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.agent = agentSample;
        agentServiceStub.update.resolves(agentSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(agentServiceStub.update.calledWith(agentSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        agentServiceStub.create.resolves(entity);
        const wrapper = shallowMount(AgentUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.agent = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(agentServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        agentServiceStub.find.resolves(agentSample);
        agentServiceStub.retrieve.resolves([agentSample]);

        // WHEN
        route = {
          params: {
            agentId: `${agentSample.id}`,
          },
        };
        const wrapper = shallowMount(AgentUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.agent).toMatchObject(agentSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        agentServiceStub.find.resolves(agentSample);
        const wrapper = shallowMount(AgentUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
