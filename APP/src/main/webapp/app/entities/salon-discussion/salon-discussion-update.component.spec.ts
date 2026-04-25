import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import dayjs from 'dayjs';
import sinon, { type SinonStubbedInstance } from 'sinon';

import UtilisateurService from '@/entities/utilisateur/utilisateur.service';
import AlertService from '@/shared/alert/alert.service';
import { DATE_TIME_LONG_FORMAT } from '@/shared/composables/date-format';

import SalonDiscussionUpdate from './salon-discussion-update.vue';
import SalonDiscussionService from './salon-discussion.service';

type SalonDiscussionUpdateComponentType = InstanceType<typeof SalonDiscussionUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const salonDiscussionSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<SalonDiscussionUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('SalonDiscussion Management Update Component', () => {
    let comp: SalonDiscussionUpdateComponentType;
    let salonDiscussionServiceStub: SinonStubbedInstance<SalonDiscussionService>;

    beforeEach(() => {
      route = {};
      salonDiscussionServiceStub = sinon.createStubInstance<SalonDiscussionService>(SalonDiscussionService);
      salonDiscussionServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          salonDiscussionService: () => salonDiscussionServiceStub,
          utilisateurService: () =>
            sinon.createStubInstance<UtilisateurService>(UtilisateurService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('load', () => {
      beforeEach(() => {
        const wrapper = shallowMount(SalonDiscussionUpdate, { global: mountOptions });
        comp = wrapper.vm;
      });
      it('Should convert date from string', () => {
        // GIVEN
        const date = new Date('2019-10-15T11:42:02Z');

        // WHEN
        const convertedDate = comp.convertDateTimeFromServer(date);

        // THEN
        expect(convertedDate).toEqual(dayjs(date).format(DATE_TIME_LONG_FORMAT));
      });

      it('Should not convert date if date is not present', () => {
        expect(comp.convertDateTimeFromServer(null)).toBeNull();
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(SalonDiscussionUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.salonDiscussion = salonDiscussionSample;
        salonDiscussionServiceStub.update.resolves(salonDiscussionSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(salonDiscussionServiceStub.update.calledWith(salonDiscussionSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        salonDiscussionServiceStub.create.resolves(entity);
        const wrapper = shallowMount(SalonDiscussionUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.salonDiscussion = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(salonDiscussionServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        salonDiscussionServiceStub.find.resolves(salonDiscussionSample);
        salonDiscussionServiceStub.retrieve.resolves([salonDiscussionSample]);

        // WHEN
        route = {
          params: {
            salonDiscussionId: `${salonDiscussionSample.id}`,
          },
        };
        const wrapper = shallowMount(SalonDiscussionUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.salonDiscussion).toMatchObject(salonDiscussionSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        salonDiscussionServiceStub.find.resolves(salonDiscussionSample);
        const wrapper = shallowMount(SalonDiscussionUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
