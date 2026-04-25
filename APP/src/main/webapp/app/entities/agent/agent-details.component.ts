import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { type IAgent } from '@/shared/model/agent.model';

import AgentService from './agent.service';

export default defineComponent({
  name: 'AgentDetails',
  setup() {
    const agentService = inject('agentService', () => new AgentService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const agent: Ref<IAgent> = ref({});

    const retrieveAgent = async agentId => {
      try {
        const res = await agentService().find(agentId);
        agent.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.agentId) {
      retrieveAgent(route.params.agentId);
    }

    return {
      alertService,
      agent,

      previousState,
      t$: useI18n().t,
    };
  },
});
