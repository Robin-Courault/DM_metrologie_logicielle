import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { useDateFormat } from '@/shared/composables';
import { type IAnnonce } from '@/shared/model/annonce.model';

import AnnonceService from './annonce.service';

export default defineComponent({
  name: 'AnnonceDetails',
  setup() {
    const dateFormat = useDateFormat();
    const annonceService = inject('annonceService', () => new AnnonceService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const annonce: Ref<IAnnonce> = ref({});

    const retrieveAnnonce = async annonceId => {
      try {
        const res = await annonceService().find(annonceId);
        annonce.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.annonceId) {
      retrieveAnnonce(route.params.annonceId);
    }

    return {
      ...dateFormat,
      alertService,
      annonce,

      previousState,
      t$: useI18n().t,
    };
  },
});
