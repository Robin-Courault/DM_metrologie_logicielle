import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import UtilisateurService from '@/entities/utilisateur/utilisateur.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import { Citoyen, type ICitoyen } from '@/shared/model/citoyen.model';
import { type IUtilisateur } from '@/shared/model/utilisateur.model';

import CitoyenService from './citoyen.service';

export default defineComponent({
  name: 'CitoyenUpdate',
  setup() {
    const citoyenService = inject('citoyenService', () => new CitoyenService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const citoyen: Ref<ICitoyen> = ref(new Citoyen());

    const utilisateurService = inject('utilisateurService', () => new UtilisateurService());

    const utilisateurs: Ref<IUtilisateur[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'fr'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveCitoyen = async citoyenId => {
      try {
        const res = await citoyenService().find(citoyenId);
        citoyen.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.citoyenId) {
      retrieveCitoyen(route.params.citoyenId);
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
    const v$ = useVuelidate(validationRules, citoyen as any);
    v$.value.$validate();

    return {
      citoyenService,
      alertService,
      citoyen,
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
      if (this.citoyen.id) {
        this.citoyenService()
          .update(this.citoyen)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('assistaCriseApp.citoyen.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.citoyenService()
          .create(this.citoyen)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('assistaCriseApp.citoyen.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
