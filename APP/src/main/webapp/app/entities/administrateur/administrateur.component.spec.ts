import { beforeEach, describe, expect, it, vitest } from 'vitest';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import AdministrateurService from './administrateur.service';
import Administrateur from './administrateur.vue';

type AdministrateurComponentType = InstanceType<typeof Administrateur>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('Administrateur Management Component', () => {
    let administrateurServiceStub: SinonStubbedInstance<AdministrateurService>;
    let mountOptions: MountingOptions<AdministrateurComponentType>['global'];

    beforeEach(() => {
      administrateurServiceStub = sinon.createStubInstance<AdministrateurService>(AdministrateurService);
      administrateurServiceStub.retrieve.resolves({ headers: {} });

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
          administrateurService: () => administrateurServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        administrateurServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(Administrateur, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(administrateurServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.administrateurs[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: AdministrateurComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(Administrateur, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        administrateurServiceStub.retrieve.reset();
        administrateurServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        administrateurServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeAdministrateur();
        await comp.$nextTick(); // clear components

        // THEN
        expect(administrateurServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(administrateurServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
