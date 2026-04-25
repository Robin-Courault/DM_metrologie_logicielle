import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import dayjs from 'dayjs';
import sinon, { type SinonStubbedInstance } from 'sinon';

import SalonDiscussionService from '@/entities/salon-discussion/salon-discussion.service';
import AlertService from '@/shared/alert/alert.service';
import { DATE_TIME_LONG_FORMAT } from '@/shared/composables/date-format';

import UtilisateurUpdate from './utilisateur-update.vue';
import UtilisateurService from './utilisateur.service';

type UtilisateurUpdateComponentType = InstanceType<typeof UtilisateurUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const utilisateurSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<UtilisateurUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Utilisateur Management Update Component', () => {
    let comp: UtilisateurUpdateComponentType;
    let utilisateurServiceStub: SinonStubbedInstance<UtilisateurService>;

    beforeEach(() => {
      route = {};
      utilisateurServiceStub = sinon.createStubInstance<UtilisateurService>(UtilisateurService);
      utilisateurServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          utilisateurService: () => utilisateurServiceStub,
          salonDiscussionService: () =>
            sinon.createStubInstance<SalonDiscussionService>(SalonDiscussionService, {
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
        const wrapper = shallowMount(UtilisateurUpdate, { global: mountOptions });
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
        const wrapper = shallowMount(UtilisateurUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.utilisateur = utilisateurSample;
        utilisateurServiceStub.update.resolves(utilisateurSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(utilisateurServiceStub.update.calledWith(utilisateurSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        utilisateurServiceStub.create.resolves(entity);
        const wrapper = shallowMount(UtilisateurUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.utilisateur = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(utilisateurServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        utilisateurServiceStub.find.resolves(utilisateurSample);
        utilisateurServiceStub.retrieve.resolves([utilisateurSample]);

        // WHEN
        route = {
          params: {
            utilisateurId: `${utilisateurSample.id}`,
          },
        };
        const wrapper = shallowMount(UtilisateurUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.utilisateur).toMatchObject(utilisateurSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        utilisateurServiceStub.find.resolves(utilisateurSample);
        const wrapper = shallowMount(UtilisateurUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
