import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import dayjs from 'dayjs';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AutoriteService from '@/entities/autorite/autorite.service';
import AlertService from '@/shared/alert/alert.service';
import { DATE_TIME_LONG_FORMAT } from '@/shared/composables/date-format';

import CriseUpdate from './crise-update.vue';
import CriseService from './crise.service';

type CriseUpdateComponentType = InstanceType<typeof CriseUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const criseSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<CriseUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Crise Management Update Component', () => {
    let comp: CriseUpdateComponentType;
    let criseServiceStub: SinonStubbedInstance<CriseService>;

    beforeEach(() => {
      route = {};
      criseServiceStub = sinon.createStubInstance<CriseService>(CriseService);
      criseServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          criseService: () => criseServiceStub,
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

    describe('load', () => {
      beforeEach(() => {
        const wrapper = shallowMount(CriseUpdate, { global: mountOptions });
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
        const wrapper = shallowMount(CriseUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.crise = criseSample;
        criseServiceStub.update.resolves(criseSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(criseServiceStub.update.calledWith(criseSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        criseServiceStub.create.resolves(entity);
        const wrapper = shallowMount(CriseUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.crise = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(criseServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        criseServiceStub.find.resolves(criseSample);
        criseServiceStub.retrieve.resolves([criseSample]);

        // WHEN
        route = {
          params: {
            criseId: `${criseSample.id}`,
          },
        };
        const wrapper = shallowMount(CriseUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.crise).toMatchObject(criseSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        criseServiceStub.find.resolves(criseSample);
        const wrapper = shallowMount(CriseUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
