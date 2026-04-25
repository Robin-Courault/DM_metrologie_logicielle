import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import dayjs from 'dayjs';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AdministrateurService from '@/entities/administrateur/administrateur.service';
import AnnonceService from '@/entities/annonce/annonce.service';
import UtilisateurService from '@/entities/utilisateur/utilisateur.service';
import AlertService from '@/shared/alert/alert.service';
import { DATE_TIME_LONG_FORMAT } from '@/shared/composables/date-format';

import ModerationActionUpdate from './moderation-action-update.vue';
import ModerationActionService from './moderation-action.service';

type ModerationActionUpdateComponentType = InstanceType<typeof ModerationActionUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const moderationActionSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<ModerationActionUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('ModerationAction Management Update Component', () => {
    let comp: ModerationActionUpdateComponentType;
    let moderationActionServiceStub: SinonStubbedInstance<ModerationActionService>;

    beforeEach(() => {
      route = {};
      moderationActionServiceStub = sinon.createStubInstance<ModerationActionService>(ModerationActionService);
      moderationActionServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          moderationActionService: () => moderationActionServiceStub,
          administrateurService: () =>
            sinon.createStubInstance<AdministrateurService>(AdministrateurService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          annonceService: () =>
            sinon.createStubInstance<AnnonceService>(AnnonceService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
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
        const wrapper = shallowMount(ModerationActionUpdate, { global: mountOptions });
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
        const wrapper = shallowMount(ModerationActionUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.moderationAction = moderationActionSample;
        moderationActionServiceStub.update.resolves(moderationActionSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(moderationActionServiceStub.update.calledWith(moderationActionSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        moderationActionServiceStub.create.resolves(entity);
        const wrapper = shallowMount(ModerationActionUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.moderationAction = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(moderationActionServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        moderationActionServiceStub.find.resolves(moderationActionSample);
        moderationActionServiceStub.retrieve.resolves([moderationActionSample]);

        // WHEN
        route = {
          params: {
            moderationActionId: `${moderationActionSample.id}`,
          },
        };
        const wrapper = shallowMount(ModerationActionUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.moderationAction).toMatchObject(moderationActionSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        moderationActionServiceStub.find.resolves(moderationActionSample);
        const wrapper = shallowMount(ModerationActionUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
