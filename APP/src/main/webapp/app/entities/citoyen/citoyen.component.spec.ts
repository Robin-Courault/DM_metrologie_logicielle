import { beforeEach, describe, expect, it, vitest } from 'vitest';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import CitoyenService from './citoyen.service';
import Citoyen from './citoyen.vue';

type CitoyenComponentType = InstanceType<typeof Citoyen>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('Citoyen Management Component', () => {
    let citoyenServiceStub: SinonStubbedInstance<CitoyenService>;
    let mountOptions: MountingOptions<CitoyenComponentType>['global'];

    beforeEach(() => {
      citoyenServiceStub = sinon.createStubInstance<CitoyenService>(CitoyenService);
      citoyenServiceStub.retrieve.resolves({ headers: {} });

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
          citoyenService: () => citoyenServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        citoyenServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(Citoyen, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(citoyenServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.citoyens[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: CitoyenComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(Citoyen, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        citoyenServiceStub.retrieve.reset();
        citoyenServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        citoyenServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeCitoyen();
        await comp.$nextTick(); // clear components

        // THEN
        expect(citoyenServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(citoyenServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
