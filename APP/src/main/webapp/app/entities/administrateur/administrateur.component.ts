import { type Ref, defineComponent, inject, onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';

import { useAlertService } from '@/shared/alert/alert.service';
import { type IAdministrateur } from '@/shared/model/administrateur.model';

import AdministrateurService from './administrateur.service';

export default defineComponent({
  name: 'Administrateur',
  setup() {
    const { t: t$ } = useI18n();
    const administrateurService = inject('administrateurService', () => new AdministrateurService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const administrateurs: Ref<IAdministrateur[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveAdministrateurs = async () => {
      isFetching.value = true;
      try {
        const res = await administrateurService().retrieve();
        administrateurs.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveAdministrateurs();
    };

    onMounted(async () => {
      await retrieveAdministrateurs();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IAdministrateur) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeAdministrateur = async () => {
      try {
        await administrateurService().delete(removeId.value);
        const message = t$('assistaCriseApp.administrateur.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveAdministrateurs();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      administrateurs,
      handleSyncList,
      isFetching,
      retrieveAdministrateurs,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeAdministrateur,
      t$,
    };
  },
});
