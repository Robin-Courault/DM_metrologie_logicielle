import { beforeEach, describe, expect, it, vitest } from 'vitest';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import SinistreService from './sinistre.service';
import Sinistre from './sinistre.vue';

type SinistreComponentType = InstanceType<typeof Sinistre>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('Sinistre Management Component', () => {
    let sinistreServiceStub: SinonStubbedInstance<SinistreService>;
    let mountOptions: MountingOptions<SinistreComponentType>['global'];

    beforeEach(() => {
      sinistreServiceStub = sinon.createStubInstance<SinistreService>(SinistreService);
      sinistreServiceStub.retrieve.resolves({ headers: {} });

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
          sinistreService: () => sinistreServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        sinistreServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(Sinistre, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(sinistreServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.sinistres[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: SinistreComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(Sinistre, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        sinistreServiceStub.retrieve.reset();
        sinistreServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        sinistreServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeSinistre();
        await comp.$nextTick(); // clear components

        // THEN
        expect(sinistreServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(sinistreServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
