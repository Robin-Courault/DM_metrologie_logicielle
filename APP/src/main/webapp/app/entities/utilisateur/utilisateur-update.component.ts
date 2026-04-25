import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import SalonDiscussionService from '@/entities/salon-discussion/salon-discussion.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import { type ISalonDiscussion } from '@/shared/model/salon-discussion.model';
import { type IUtilisateur, Utilisateur } from '@/shared/model/utilisateur.model';

import UtilisateurService from './utilisateur.service';

export default defineComponent({
  name: 'UtilisateurUpdate',
  setup() {
    const utilisateurService = inject('utilisateurService', () => new UtilisateurService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const utilisateur: Ref<IUtilisateur> = ref(new Utilisateur());

    const salonDiscussionService = inject('salonDiscussionService', () => new SalonDiscussionService());

    const salonDiscussions: Ref<ISalonDiscussion[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'fr'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveUtilisateur = async utilisateurId => {
      try {
        const res = await utilisateurService().find(utilisateurId);
        res.dateInscription = new Date(res.dateInscription);
        utilisateur.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.utilisateurId) {
      retrieveUtilisateur(route.params.utilisateurId);
    }

    const initRelationships = () => {
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
      login: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      nom: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      prenom: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      email: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      telephone: {},
      motDePasse: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      dateInscription: {},
      actif: {},
      banni: {},
      salonses: {},
      sinistre: {},
      citoyen: {},
      agent: {},
      administrateur: {},
    };
    const v$ = useVuelidate(validationRules, utilisateur as any);
    v$.value.$validate();

    return {
      utilisateurService,
      alertService,
      utilisateur,
      previousState,
      isSaving,
      currentLanguage,
      salonDiscussions,
      v$,
      ...useDateFormat({ entityRef: utilisateur }),
      t$,
    };
  },
  created(): void {
    this.utilisateur.salonses = [];
  },
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.utilisateur.id) {
        this.utilisateurService()
          .update(this.utilisateur)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('assistaCriseApp.utilisateur.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.utilisateurService()
          .create(this.utilisateur)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('assistaCriseApp.utilisateur.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },

    getSelected(selectedVals, option, pkField = 'id'): any {
      if (selectedVals) {
        return selectedVals.find(value => option[pkField] === value[pkField]) ?? option;
      }
      return option;
    },
  },
});
