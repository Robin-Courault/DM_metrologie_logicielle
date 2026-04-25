import { type Ref, defineComponent, inject, onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';

import { useAlertService } from '@/shared/alert/alert.service';
import { useDateFormat } from '@/shared/composables';
import { type ISalonDiscussion } from '@/shared/model/salon-discussion.model';

import SalonDiscussionService from './salon-discussion.service';

export default defineComponent({
  name: 'SalonDiscussion',
  setup() {
    const { t: t$ } = useI18n();
    const dateFormat = useDateFormat();
    const salonDiscussionService = inject('salonDiscussionService', () => new SalonDiscussionService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const salonDiscussions: Ref<ISalonDiscussion[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveSalonDiscussions = async () => {
      isFetching.value = true;
      try {
        const res = await salonDiscussionService().retrieve();
        salonDiscussions.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveSalonDiscussions();
    };

    onMounted(async () => {
      await retrieveSalonDiscussions();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: ISalonDiscussion) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeSalonDiscussion = async () => {
      try {
        await salonDiscussionService().delete(removeId.value);
        const message = t$('assistaCriseApp.salonDiscussion.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveSalonDiscussions();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      salonDiscussions,
      handleSyncList,
      isFetching,
      retrieveSalonDiscussions,
      clear,
      ...dateFormat,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeSalonDiscussion,
      t$,
    };
  },
});
