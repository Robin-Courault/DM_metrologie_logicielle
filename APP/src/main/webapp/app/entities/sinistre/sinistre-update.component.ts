import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import UtilisateurService from '@/entities/utilisateur/utilisateur.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import { type ISinistre, Sinistre } from '@/shared/model/sinistre.model';
import { type IUtilisateur } from '@/shared/model/utilisateur.model';

import SinistreService from './sinistre.service';

export default defineComponent({
  name: 'SinistreUpdate',
  setup() {
    const sinistreService = inject('sinistreService', () => new SinistreService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const sinistre: Ref<ISinistre> = ref(new Sinistre());

    const utilisateurService = inject('utilisateurService', () => new UtilisateurService());

    const utilisateurs: Ref<IUtilisateur[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'fr'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveSinistre = async sinistreId => {
      try {
        const res = await sinistreService().find(sinistreId);
        sinistre.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.sinistreId) {
      retrieveSinistre(route.params.sinistreId);
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
      utilisateur: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
    };
    const v$ = useVuelidate(validationRules, sinistre as any);
    v$.value.$validate();

    return {
      sinistreService,
      alertService,
      sinistre,
      previousState,
      isSaving,
      currentLanguage,
      utilisateurs,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.sinistre.id) {
        this.sinistreService()
          .update(this.sinistre)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('assistaCriseApp.sinistre.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.sinistreService()
          .create(this.sinistre)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('assistaCriseApp.sinistre.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
