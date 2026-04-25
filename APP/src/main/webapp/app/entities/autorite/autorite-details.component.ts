import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { type IAutorite } from '@/shared/model/autorite.model';

import AutoriteService from './autorite.service';

export default defineComponent({
  name: 'AutoriteDetails',
  setup() {
    const autoriteService = inject('autoriteService', () => new AutoriteService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const autorite: Ref<IAutorite> = ref({});

    const retrieveAutorite = async autoriteId => {
      try {
        const res = await autoriteService().find(autoriteId);
        autorite.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.autoriteId) {
      retrieveAutorite(route.params.autoriteId);
    }

    return {
      alertService,
      autorite,

      previousState,
      t$: useI18n().t,
    };
  },
});
