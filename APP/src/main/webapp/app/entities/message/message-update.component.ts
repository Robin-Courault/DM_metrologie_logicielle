import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import SalonDiscussionService from '@/entities/salon-discussion/salon-discussion.service';
import UtilisateurService from '@/entities/utilisateur/utilisateur.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import { type IMessage, Message } from '@/shared/model/message.model';
import { type ISalonDiscussion } from '@/shared/model/salon-discussion.model';
import { type IUtilisateur } from '@/shared/model/utilisateur.model';

import MessageService from './message.service';

export default defineComponent({
  name: 'MessageUpdate',
  setup() {
    const messageService = inject('messageService', () => new MessageService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const message: Ref<IMessage> = ref(new Message());

    const utilisateurService = inject('utilisateurService', () => new UtilisateurService());

    const utilisateurs: Ref<IUtilisateur[]> = ref([]);

    const salonDiscussionService = inject('salonDiscussionService', () => new SalonDiscussionService());

    const salonDiscussions: Ref<ISalonDiscussion[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'fr'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveMessage = async messageId => {
      try {
        const res = await messageService().find(messageId);
        res.dateEnvoi = new Date(res.dateEnvoi);
        message.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.messageId) {
      retrieveMessage(route.params.messageId);
    }

    const initRelationships = () => {
      utilisateurService()
        .retrieve()
        .then(res => {
          utilisateurs.value = res.data;
        });
      salonDiscussionService()
        .retrieve()
        .then(res => {
          salonDiscussions.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      contenu: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      dateEnvoi: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      utilisateur: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      salonDiscussion: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
    };
    const v$ = useVuelidate(validationRules, message as any);
    v$.value.$validate();

    return {
      messageService,
      alertService,
      message,
      previousState,
      isSaving,
      currentLanguage,
      utilisateurs,
      salonDiscussions,
      v$,
      ...useDateFormat({ entityRef: message }),
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.message.id) {
        this.messageService()
          .update(this.message)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('assistaCriseApp.message.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.messageService()
          .create(this.message)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('assistaCriseApp.message.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
