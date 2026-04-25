import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { useDateFormat } from '@/shared/composables';
import { type IModerationAction } from '@/shared/model/moderation-action.model';

import ModerationActionService from './moderation-action.service';

export default defineComponent({
  name: 'ModerationActionDetails',
  setup() {
    const dateFormat = useDateFormat();
    const moderationActionService = inject('moderationActionService', () => new ModerationActionService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const moderationAction: Ref<IModerationAction> = ref({});

    const retrieveModerationAction = async moderationActionId => {
      try {
        const res = await moderationActionService().find(moderationActionId);
        moderationAction.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.moderationActionId) {
      retrieveModerationAction(route.params.moderationActionId);
    }

    return {
      ...dateFormat,
      alertService,
      moderationAction,

      previousState,
      t$: useI18n().t,
    };
  },
});
