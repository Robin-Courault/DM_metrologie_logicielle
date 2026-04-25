import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import dayjs from 'dayjs';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';
import { DATE_TIME_LONG_FORMAT } from '@/shared/composables/date-format';

import AnnonceUpdate from './annonce-update.vue';
import AnnonceService from './annonce.service';

type AnnonceUpdateComponentType = InstanceType<typeof AnnonceUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const annonceSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<AnnonceUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Annonce Management Update Component', () => {
    let comp: AnnonceUpdateComponentType;
    let annonceServiceStub: SinonStubbedInstance<AnnonceService>;

    beforeEach(() => {
      route = {};
      annonceServiceStub = sinon.createStubInstance<AnnonceService>(AnnonceService);
      annonceServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          annonceService: () => annonceServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('load', () => {
      beforeEach(() => {
        const wrapper = shallowMount(AnnonceUpdate, { global: mountOptions });
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
        const wrapper = shallowMount(AnnonceUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.annonce = annonceSample;
        annonceServiceStub.update.resolves(annonceSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(annonceServiceStub.update.calledWith(annonceSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        annonceServiceStub.create.resolves(entity);
        const wrapper = shallowMount(AnnonceUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.annonce = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(annonceServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        annonceServiceStub.find.resolves(annonceSample);
        annonceServiceStub.retrieve.resolves([annonceSample]);

        // WHEN
        route = {
          params: {
            annonceId: `${annonceSample.id}`,
          },
        };
        const wrapper = shallowMount(AnnonceUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.annonce).toMatchObject(annonceSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        annonceServiceStub.find.resolves(annonceSample);
        const wrapper = shallowMount(AnnonceUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
