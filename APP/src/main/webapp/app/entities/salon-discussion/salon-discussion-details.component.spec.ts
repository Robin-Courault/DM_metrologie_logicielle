import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import SalonDiscussionDetails from './salon-discussion-details.vue';
import SalonDiscussionService from './salon-discussion.service';

type SalonDiscussionDetailsComponentType = InstanceType<typeof SalonDiscussionDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const salonDiscussionSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('SalonDiscussion Management Detail Component', () => {
    let salonDiscussionServiceStub: SinonStubbedInstance<SalonDiscussionService>;
    let mountOptions: MountingOptions<SalonDiscussionDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      salonDiscussionServiceStub = sinon.createStubInstance<SalonDiscussionService>(SalonDiscussionService);

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
          salonDiscussionService: () => salonDiscussionServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        salonDiscussionServiceStub.find.resolves(salonDiscussionSample);
        route = {
          params: {
            salonDiscussionId: `${123}`,
          },
        };
        const wrapper = shallowMount(SalonDiscussionDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.salonDiscussion).toMatchObject(salonDiscussionSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        salonDiscussionServiceStub.find.resolves(salonDiscussionSample);
        const wrapper = shallowMount(SalonDiscussionDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
