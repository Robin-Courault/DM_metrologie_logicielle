import { type Ref, defineComponent, inject, onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';

import { useAlertService } from '@/shared/alert/alert.service';
import { type ICitoyen } from '@/shared/model/citoyen.model';

import CitoyenService from './citoyen.service';

export default defineComponent({
  name: 'Citoyen',
  setup() {
    const { t: t$ } = useI18n();
    const citoyenService = inject('citoyenService', () => new CitoyenService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const citoyens: Ref<ICitoyen[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveCitoyens = async () => {
      isFetching.value = true;
      try {
        const res = await citoyenService().retrieve();
        citoyens.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveCitoyens();
    };

    onMounted(async () => {
      await retrieveCitoyens();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: ICitoyen) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeCitoyen = async () => {
      try {
        await citoyenService().delete(removeId.value);
        const message = t$('assistaCriseApp.citoyen.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveCitoyens();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      citoyens,
      handleSyncList,
      isFetching,
      retrieveCitoyens,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeCitoyen,
      t$,
    };
  },
});
