import { type Ref, defineComponent, inject, onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';

import { useAlertService } from '@/shared/alert/alert.service';
import { type IAgent } from '@/shared/model/agent.model';

import AgentService from './agent.service';

export default defineComponent({
  name: 'Agent',
  setup() {
    const { t: t$ } = useI18n();
    const agentService = inject('agentService', () => new AgentService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const agents: Ref<IAgent[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveAgents = async () => {
      isFetching.value = true;
      try {
        const res = await agentService().retrieve();
        agents.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveAgents();
    };

    onMounted(async () => {
      await retrieveAgents();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IAgent) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeAgent = async () => {
      try {
        await agentService().delete(removeId.value);
        const message = t$('assistaCriseApp.agent.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveAgents();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      agents,
      handleSyncList,
      isFetching,
      retrieveAgents,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeAgent,
      t$,
    };
  },
});
