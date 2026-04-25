import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import ModerationActionDetails from './moderation-action-details.vue';
import ModerationActionService from './moderation-action.service';

type ModerationActionDetailsComponentType = InstanceType<typeof ModerationActionDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const moderationActionSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('ModerationAction Management Detail Component', () => {
    let moderationActionServiceStub: SinonStubbedInstance<ModerationActionService>;
    let mountOptions: MountingOptions<ModerationActionDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      moderationActionServiceStub = sinon.createStubInstance<ModerationActionService>(ModerationActionService);

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
          moderationActionService: () => moderationActionServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        moderationActionServiceStub.find.resolves(moderationActionSample);
        route = {
          params: {
            moderationActionId: `${123}`,
          },
        };
        const wrapper = shallowMount(ModerationActionDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.moderationAction).toMatchObject(moderationActionSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        moderationActionServiceStub.find.resolves(moderationActionSample);
        const wrapper = shallowMount(ModerationActionDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
