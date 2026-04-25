import { type Ref, defineComponent, inject, onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';

import { useAlertService } from '@/shared/alert/alert.service';
import { type ISinistre } from '@/shared/model/sinistre.model';

import SinistreService from './sinistre.service';

export default defineComponent({
  name: 'Sinistre',
  setup() {
    const { t: t$ } = useI18n();
    const sinistreService = inject('sinistreService', () => new SinistreService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const sinistres: Ref<ISinistre[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveSinistres = async () => {
      isFetching.value = true;
      try {
        const res = await sinistreService().retrieve();
        sinistres.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveSinistres();
    };

    onMounted(async () => {
      await retrieveSinistres();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: ISinistre) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeSinistre = async () => {
      try {
        await sinistreService().delete(removeId.value);
        const message = t$('assistaCriseApp.sinistre.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveSinistres();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      sinistres,
      handleSyncList,
      isFetching,
      retrieveSinistres,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeSinistre,
      t$,
    };
  },
});
