import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import UtilisateurService from '@/entities/utilisateur/utilisateur.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import { Administrateur, type IAdministrateur } from '@/shared/model/administrateur.model';
import { type IUtilisateur } from '@/shared/model/utilisateur.model';

import AdministrateurService from './administrateur.service';

export default defineComponent({
  name: 'AdministrateurUpdate',
  setup() {
    const administrateurService = inject('administrateurService', () => new AdministrateurService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const administrateur: Ref<IAdministrateur> = ref(new Administrateur());

    const utilisateurService = inject('utilisateurService', () => new UtilisateurService());

    const utilisateurs: Ref<IUtilisateur[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'fr'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveAdministrateur = async administrateurId => {
      try {
        const res = await administrateurService().find(administrateurId);
        administrateur.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.administrateurId) {
      retrieveAdministrateur(route.params.administrateurId);
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
    const v$ = useVuelidate(validationRules, administrateur as any);
    v$.value.$validate();

    return {
      administrateurService,
      alertService,
      administrateur,
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
      if (this.administrateur.id) {
        this.administrateurService()
          .update(this.administrateur)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('assistaCriseApp.administrateur.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.administrateurService()
          .create(this.administrateur)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('assistaCriseApp.administrateur.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
