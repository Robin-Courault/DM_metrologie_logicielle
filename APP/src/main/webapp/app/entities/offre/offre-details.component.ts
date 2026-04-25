import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { useDateFormat } from '@/shared/composables';
import { type IOffre } from '@/shared/model/offre.model';

import OffreService from './offre.service';

export default defineComponent({
  name: 'OffreDetails',
  setup() {
    const dateFormat = useDateFormat();
    const offreService = inject('offreService', () => new OffreService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const offre: Ref<IOffre> = ref({});

    const retrieveOffre = async offreId => {
      try {
        const res = await offreService().find(offreId);
        offre.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.offreId) {
      retrieveOffre(route.params.offreId);
    }

    return {
      ...dateFormat,
      alertService,
      offre,

      previousState,
      t$: useI18n().t,
    };
  },
});
