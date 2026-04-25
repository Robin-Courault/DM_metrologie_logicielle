import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { useDateFormat } from '@/shared/composables';
import { type IUtilisateur } from '@/shared/model/utilisateur.model';

import UtilisateurService from './utilisateur.service';

export default defineComponent({
  name: 'UtilisateurDetails',
  setup() {
    const dateFormat = useDateFormat();
    const utilisateurService = inject('utilisateurService', () => new UtilisateurService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const utilisateur: Ref<IUtilisateur> = ref({});

    const retrieveUtilisateur = async utilisateurId => {
      try {
        const res = await utilisateurService().find(utilisateurId);
        utilisateur.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.utilisateurId) {
      retrieveUtilisateur(route.params.utilisateurId);
    }

    return {
      ...dateFormat,
      alertService,
      utilisateur,

      previousState,
      t$: useI18n().t,
    };
  },
});
