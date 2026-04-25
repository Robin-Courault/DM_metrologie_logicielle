import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { type IAdministrateur } from '@/shared/model/administrateur.model';

import AdministrateurService from './administrateur.service';

export default defineComponent({
  name: 'AdministrateurDetails',
  setup() {
    const administrateurService = inject('administrateurService', () => new AdministrateurService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const administrateur: Ref<IAdministrateur> = ref({});

    const retrieveAdministrateur = async administrateurId => {
      try {
        const res = await administrateurService().find(administrateurId);
        administrateur.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.administrateurId) {
      retrieveAdministrateur(route.params.administrateurId);
    }

    return {
      alertService,
      administrateur,

      previousState,
      t$: useI18n().t,
    };
  },
});
