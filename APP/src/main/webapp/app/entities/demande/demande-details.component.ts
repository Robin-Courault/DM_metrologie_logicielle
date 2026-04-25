import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { useDateFormat } from '@/shared/composables';
import { type IDemande } from '@/shared/model/demande.model';

import DemandeService from './demande.service';

export default defineComponent({
  name: 'DemandeDetails',
  setup() {
    const dateFormat = useDateFormat();
    const demandeService = inject('demandeService', () => new DemandeService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const demande: Ref<IDemande> = ref({});

    const retrieveDemande = async demandeId => {
      try {
        const res = await demandeService().find(demandeId);
        demande.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.demandeId) {
      retrieveDemande(route.params.demandeId);
    }

    return {
      ...dateFormat,
      alertService,
      demande,

      previousState,
      t$: useI18n().t,
    };
  },
});
