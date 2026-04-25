import { beforeEach, describe, expect, it, vitest } from 'vitest';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import UtilisateurService from './utilisateur.service';
import Utilisateur from './utilisateur.vue';

type UtilisateurComponentType = InstanceType<typeof Utilisateur>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('Utilisateur Management Component', () => {
    let utilisateurServiceStub: SinonStubbedInstance<UtilisateurService>;
    let mountOptions: MountingOptions<UtilisateurComponentType>['global'];

    beforeEach(() => {
      utilisateurServiceStub = sinon.createStubInstance<UtilisateurService>(UtilisateurService);
      utilisateurServiceStub.retrieve.resolves({ headers: {} });

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
          utilisateurService: () => utilisateurServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        utilisateurServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(Utilisateur, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(utilisateurServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.utilisateurs[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: UtilisateurComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(Utilisateur, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        utilisateurServiceStub.retrieve.reset();
        utilisateurServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        utilisateurServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeUtilisateur();
        await comp.$nextTick(); // clear components

        // THEN
        expect(utilisateurServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(utilisateurServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
