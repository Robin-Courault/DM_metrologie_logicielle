import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import { useAlertService } from '@/shared/alert/alert.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import { Annonce, type IAnnonce } from '@/shared/model/annonce.model';
import { CategorieBesoin } from '@/shared/model/enumerations/categorie-besoin.model';
import { EtatAnnonce } from '@/shared/model/enumerations/etat-annonce.model';

import AnnonceService from './annonce.service';

export default defineComponent({
  name: 'AnnonceUpdate',
  setup() {
    const annonceService = inject('annonceService', () => new AnnonceService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const annonce: Ref<IAnnonce> = ref(new Annonce());
    const categorieBesoinValues: Ref<string[]> = ref(Object.keys(CategorieBesoin));
    const etatAnnonceValues: Ref<string[]> = ref(Object.keys(EtatAnnonce));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'fr'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveAnnonce = async annonceId => {
      try {
        const res = await annonceService().find(annonceId);
        res.dateCreation = new Date(res.dateCreation);
        res.dateMaJ = new Date(res.dateMaJ);
        annonce.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.annonceId) {
      retrieveAnnonce(route.params.annonceId);
    }

    const initRelationships = () => {};

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      titre: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      description: {},
      categorie: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      latitude: {},
      longitude: {},
      adresse: {},
      dateCreation: {},
      dateMaJ: {},
      etat: {},
      demande: {},
      offre: {},
    };
    const v$ = useVuelidate(validationRules, annonce as any);
    v$.value.$validate();

    return {
      annonceService,
      alertService,
      annonce,
      previousState,
      categorieBesoinValues,
      etatAnnonceValues,
      isSaving,
      currentLanguage,
      v$,
      ...useDateFormat({ entityRef: annonce }),
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.annonce.id) {
        this.annonceService()
          .update(this.annonce)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('assistaCriseApp.annonce.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.annonceService()
          .create(this.annonce)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('assistaCriseApp.annonce.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
