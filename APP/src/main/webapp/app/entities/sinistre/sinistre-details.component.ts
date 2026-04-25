import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { type ISinistre } from '@/shared/model/sinistre.model';

import SinistreService from './sinistre.service';

export default defineComponent({
  name: 'SinistreDetails',
  setup() {
    const sinistreService = inject('sinistreService', () => new SinistreService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const sinistre: Ref<ISinistre> = ref({});

    const retrieveSinistre = async sinistreId => {
      try {
        const res = await sinistreService().find(sinistreId);
        sinistre.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.sinistreId) {
      retrieveSinistre(route.params.sinistreId);
    }

    return {
      alertService,
      sinistre,

      previousState,
      t$: useI18n().t,
    };
  },
});
