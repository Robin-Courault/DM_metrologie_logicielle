import { type Ref, defineComponent, inject, onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';

import { useAlertService } from '@/shared/alert/alert.service';
import { type IAutorite } from '@/shared/model/autorite.model';

import AutoriteService from './autorite.service';

export default defineComponent({
  name: 'Autorite',
  setup() {
    const { t: t$ } = useI18n();
    const autoriteService = inject('autoriteService', () => new AutoriteService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const autorites: Ref<IAutorite[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveAutorites = async () => {
      isFetching.value = true;
      try {
        const res = await autoriteService().retrieve();
        autorites.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveAutorites();
    };

    onMounted(async () => {
      await retrieveAutorites();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IAutorite) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeAutorite = async () => {
      try {
        await autoriteService().delete(removeId.value);
        const message = t$('assistaCriseApp.autorite.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveAutorites();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      autorites,
      handleSyncList,
      isFetching,
      retrieveAutorites,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeAutorite,
      t$,
    };
  },
});
