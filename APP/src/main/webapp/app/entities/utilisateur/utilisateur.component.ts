import { type Ref, defineComponent, inject, onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';

import { useAlertService } from '@/shared/alert/alert.service';
import { useDateFormat } from '@/shared/composables';
import { type IUtilisateur } from '@/shared/model/utilisateur.model';

import UtilisateurService from './utilisateur.service';

export default defineComponent({
  name: 'Utilisateur',
  setup() {
    const { t: t$ } = useI18n();
    const dateFormat = useDateFormat();
    const utilisateurService = inject('utilisateurService', () => new UtilisateurService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const utilisateurs: Ref<IUtilisateur[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveUtilisateurs = async () => {
      isFetching.value = true;
      try {
        const res = await utilisateurService().retrieve();
        utilisateurs.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveUtilisateurs();
    };

    onMounted(async () => {
      await retrieveUtilisateurs();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IUtilisateur) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeUtilisateur = async () => {
      try {
        await utilisateurService().delete(removeId.value);
        const message = t$('assistaCriseApp.utilisateur.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveUtilisateurs();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      utilisateurs,
      handleSyncList,
      isFetching,
      retrieveUtilisateurs,
      clear,
      ...dateFormat,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeUtilisateur,
      t$,
    };
  },
});
