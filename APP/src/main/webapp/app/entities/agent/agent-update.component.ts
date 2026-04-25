import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import AutoriteService from '@/entities/autorite/autorite.service';
import UtilisateurService from '@/entities/utilisateur/utilisateur.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import { Agent, type IAgent } from '@/shared/model/agent.model';
import { type IAutorite } from '@/shared/model/autorite.model';
import { type IUtilisateur } from '@/shared/model/utilisateur.model';

import AgentService from './agent.service';

export default defineComponent({
  name: 'AgentUpdate',
  setup() {
    const agentService = inject('agentService', () => new AgentService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const agent: Ref<IAgent> = ref(new Agent());

    const utilisateurService = inject('utilisateurService', () => new UtilisateurService());

    const utilisateurs: Ref<IUtilisateur[]> = ref([]);

    const autoriteService = inject('autoriteService', () => new AutoriteService());

    const autorites: Ref<IAutorite[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'fr'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

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

    const initRelationships = () => {
      utilisateurService()
        .retrieve()
        .then(res => {
          utilisateurs.value = res.data;
        });
      autoriteService()
        .retrieve()
        .then(res => {
          autorites.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      fonction: {},
      service: {},
      utilisateur: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      autorite: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
    };
    const v$ = useVuelidate(validationRules, agent as any);
    v$.value.$validate();

    return {
      agentService,
      alertService,
      agent,
      previousState,
      isSaving,
      currentLanguage,
      utilisateurs,
      autorites,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.agent.id) {
        this.agentService()
          .update(this.agent)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('assistaCriseApp.agent.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.agentService()
          .create(this.agent)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('assistaCriseApp.agent.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
