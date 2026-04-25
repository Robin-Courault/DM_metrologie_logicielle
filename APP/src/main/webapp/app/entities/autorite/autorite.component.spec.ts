import { beforeEach, describe, expect, it, vitest } from 'vitest';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import AutoriteService from './autorite.service';
import Autorite from './autorite.vue';

type AutoriteComponentType = InstanceType<typeof Autorite>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('Autorite Management Component', () => {
    let autoriteServiceStub: SinonStubbedInstance<AutoriteService>;
    let mountOptions: MountingOptions<AutoriteComponentType>['global'];

    beforeEach(() => {
      autoriteServiceStub = sinon.createStubInstance<AutoriteService>(AutoriteService);
      autoriteServiceStub.retrieve.resolves({ headers: {} });

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
          autoriteService: () => autoriteServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        autoriteServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(Autorite, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(autoriteServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.autorites[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: AutoriteComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(Autorite, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        autoriteServiceStub.retrieve.reset();
        autoriteServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        autoriteServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeAutorite();
        await comp.$nextTick(); // clear components

        // THEN
        expect(autoriteServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(autoriteServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
