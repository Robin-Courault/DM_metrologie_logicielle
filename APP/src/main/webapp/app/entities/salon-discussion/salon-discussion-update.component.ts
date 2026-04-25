import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import UtilisateurService from '@/entities/utilisateur/utilisateur.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import { type ISalonDiscussion, SalonDiscussion } from '@/shared/model/salon-discussion.model';
import { type IUtilisateur } from '@/shared/model/utilisateur.model';

import SalonDiscussionService from './salon-discussion.service';

export default defineComponent({
  name: 'SalonDiscussionUpdate',
  setup() {
    const salonDiscussionService = inject('salonDiscussionService', () => new SalonDiscussionService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const salonDiscussion: Ref<ISalonDiscussion> = ref(new SalonDiscussion());

    const utilisateurService = inject('utilisateurService', () => new UtilisateurService());

    const utilisateurs: Ref<IUtilisateur[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'fr'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveSalonDiscussion = async salonDiscussionId => {
      try {
        const res = await salonDiscussionService().find(salonDiscussionId);
        res.dateOuverture = new Date(res.dateOuverture);
        salonDiscussion.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.salonDiscussionId) {
      retrieveSalonDiscussion(route.params.salonDiscussionId);
    }

    const initRelationships = () => {
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
      dateOuverture: {},
      ouvert: {},
      participantses: {},
      demande: {},
    };
    const v$ = useVuelidate(validationRules, salonDiscussion as any);
    v$.value.$validate();

    return {
      salonDiscussionService,
      alertService,
      salonDiscussion,
      previousState,
      isSaving,
      currentLanguage,
      utilisateurs,
      v$,
      ...useDateFormat({ entityRef: salonDiscussion }),
      t$,
    };
  },
  created(): void {
    this.salonDiscussion.participantses = [];
  },
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.salonDiscussion.id) {
        this.salonDiscussionService()
          .update(this.salonDiscussion)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('assistaCriseApp.salonDiscussion.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.salonDiscussionService()
          .create(this.salonDiscussion)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('assistaCriseApp.salonDiscussion.created', { param: param.id }).toString());
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
