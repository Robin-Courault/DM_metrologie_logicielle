import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { useDateFormat } from '@/shared/composables';
import { type ISalonDiscussion } from '@/shared/model/salon-discussion.model';

import SalonDiscussionService from './salon-discussion.service';

export default defineComponent({
  name: 'SalonDiscussionDetails',
  setup() {
    const dateFormat = useDateFormat();
    const salonDiscussionService = inject('salonDiscussionService', () => new SalonDiscussionService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const salonDiscussion: Ref<ISalonDiscussion> = ref({});

    const retrieveSalonDiscussion = async salonDiscussionId => {
      try {
        const res = await salonDiscussionService().find(salonDiscussionId);
        salonDiscussion.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.salonDiscussionId) {
      retrieveSalonDiscussion(route.params.salonDiscussionId);
    }

    return {
      ...dateFormat,
      alertService,
      salonDiscussion,

      previousState,
      t$: useI18n().t,
    };
  },
});
