import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { useDateFormat } from '@/shared/composables';
import { type ICrise } from '@/shared/model/crise.model';

import CriseService from './crise.service';

export default defineComponent({
  name: 'CriseDetails',
  setup() {
    const dateFormat = useDateFormat();
    const criseService = inject('criseService', () => new CriseService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const crise: Ref<ICrise> = ref({});

    const retrieveCrise = async criseId => {
      try {
        const res = await criseService().find(criseId);
        crise.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.criseId) {
      retrieveCrise(route.params.criseId);
    }

    return {
      ...dateFormat,
      alertService,
      crise,

      previousState,
      t$: useI18n().t,
    };
  },
});
