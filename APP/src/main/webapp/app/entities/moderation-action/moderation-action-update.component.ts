import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import AdministrateurService from '@/entities/administrateur/administrateur.service';
import AnnonceService from '@/entities/annonce/annonce.service';
import UtilisateurService from '@/entities/utilisateur/utilisateur.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import { type IAdministrateur } from '@/shared/model/administrateur.model';
import { type IAnnonce } from '@/shared/model/annonce.model';
import { TypeModeration } from '@/shared/model/enumerations/type-moderation.model';
import { type IModerationAction, ModerationAction } from '@/shared/model/moderation-action.model';
import { type IUtilisateur } from '@/shared/model/utilisateur.model';

import ModerationActionService from './moderation-action.service';

export default defineComponent({
  name: 'ModerationActionUpdate',
  setup() {
    const moderationActionService = inject('moderationActionService', () => new ModerationActionService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const moderationAction: Ref<IModerationAction> = ref(new ModerationAction());

    const administrateurService = inject('administrateurService', () => new AdministrateurService());

    const administrateurs: Ref<IAdministrateur[]> = ref([]);

    const annonceService = inject('annonceService', () => new AnnonceService());

    const annonces: Ref<IAnnonce[]> = ref([]);

    const utilisateurService = inject('utilisateurService', () => new UtilisateurService());

    const utilisateurs: Ref<IUtilisateur[]> = ref([]);
    const typeModerationValues: Ref<string[]> = ref(Object.keys(TypeModeration));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'fr'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveModerationAction = async moderationActionId => {
      try {
        const res = await moderationActionService().find(moderationActionId);
        res.dateAction = new Date(res.dateAction);
        moderationAction.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.moderationActionId) {
      retrieveModerationAction(route.params.moderationActionId);
    }

    const initRelationships = () => {
      administrateurService()
        .retrieve()
        .then(res => {
          administrateurs.value = res.data;
        });
      annonceService()
        .retrieve()
        .then(res => {
          annonces.value = res.data;
        });
      utilisateurService()
        .retrieve()
        .then(res => {
          utilisateurs.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      dateAction: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      motif: {},
      type: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      administrateur: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      annonce: {},
      utilisateurCible: {},
    };
    const v$ = useVuelidate(validationRules, moderationAction as any);
    v$.value.$validate();

    return {
      moderationActionService,
      alertService,
      moderationAction,
      previousState,
      typeModerationValues,
      isSaving,
      currentLanguage,
      administrateurs,
      annonces,
      utilisateurs,
      v$,
      ...useDateFormat({ entityRef: moderationAction }),
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.moderationAction.id) {
        this.moderationActionService()
          .update(this.moderationAction)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('assistaCriseApp.moderationAction.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.moderationActionService()
          .create(this.moderationAction)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('assistaCriseApp.moderationAction.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
