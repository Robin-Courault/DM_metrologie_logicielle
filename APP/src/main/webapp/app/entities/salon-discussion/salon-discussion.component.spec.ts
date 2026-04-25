import { beforeEach, describe, expect, it, vitest } from 'vitest';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import SalonDiscussionService from './salon-discussion.service';
import SalonDiscussion from './salon-discussion.vue';

type SalonDiscussionComponentType = InstanceType<typeof SalonDiscussion>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('SalonDiscussion Management Component', () => {
    let salonDiscussionServiceStub: SinonStubbedInstance<SalonDiscussionService>;
    let mountOptions: MountingOptions<SalonDiscussionComponentType>['global'];

    beforeEach(() => {
      salonDiscussionServiceStub = sinon.createStubInstance<SalonDiscussionService>(SalonDiscussionService);
      salonDiscussionServiceStub.retrieve.resolves({ headers: {} });

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
          salonDiscussionService: () => salonDiscussionServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        salonDiscussionServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(SalonDiscussion, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(salonDiscussionServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.salonDiscussions[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: SalonDiscussionComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(SalonDiscussion, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        salonDiscussionServiceStub.retrieve.reset();
        salonDiscussionServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        salonDiscussionServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeSalonDiscussion();
        await comp.$nextTick(); // clear components

        // THEN
        expect(salonDiscussionServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(salonDiscussionServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
