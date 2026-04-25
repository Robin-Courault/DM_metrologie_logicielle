import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { type ICitoyen } from '@/shared/model/citoyen.model';

import CitoyenService from './citoyen.service';

export default defineComponent({
  name: 'CitoyenDetails',
  setup() {
    const citoyenService = inject('citoyenService', () => new CitoyenService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const citoyen: Ref<ICitoyen> = ref({});

    const retrieveCitoyen = async citoyenId => {
      try {
        const res = await citoyenService().find(citoyenId);
        citoyen.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.citoyenId) {
      retrieveCitoyen(route.params.citoyenId);
    }

    return {
      alertService,
      citoyen,

      previousState,
      t$: useI18n().t,
    };
  },
});
