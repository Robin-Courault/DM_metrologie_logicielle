import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import AnnonceService from '@/entities/annonce/annonce.service';
import CitoyenService from '@/entities/citoyen/citoyen.service';
import CriseService from '@/entities/crise/crise.service';
import DemandeService from '@/entities/demande/demande.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import { type IAnnonce } from '@/shared/model/annonce.model';
import { type ICitoyen } from '@/shared/model/citoyen.model';
import { type ICrise } from '@/shared/model/crise.model';
import { type IDemande } from '@/shared/model/demande.model';
import { type IOffre, Offre } from '@/shared/model/offre.model';

import OffreService from './offre.service';

export default defineComponent({
  name: 'OffreUpdate',
  setup() {
    const offreService = inject('offreService', () => new OffreService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const offre: Ref<IOffre> = ref(new Offre());

    const annonceService = inject('annonceService', () => new AnnonceService());

    const annonces: Ref<IAnnonce[]> = ref([]);

    const citoyenService = inject('citoyenService', () => new CitoyenService());

    const citoyens: Ref<ICitoyen[]> = ref([]);

    const criseService = inject('criseService', () => new CriseService());

    const crises: Ref<ICrise[]> = ref([]);

    const demandeService = inject('demandeService', () => new DemandeService());

    const demandes: Ref<IDemande[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'fr'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveOffre = async offreId => {
      try {
        const res = await offreService().find(offreId);
        res.disponibleDe = new Date(res.disponibleDe);
        res.disponibleJusqua = new Date(res.disponibleJusqua);
        offre.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.offreId) {
      retrieveOffre(route.params.offreId);
    }

    const initRelationships = () => {
      annonceService()
        .retrieve()
        .then(res => {
          annonces.value = res.data;
        });
      citoyenService()
        .retrieve()
        .then(res => {
          citoyens.value = res.data;
        });
      criseService()
        .retrieve()
        .then(res => {
          crises.value = res.data;
        });
      demandeService()
        .retrieve()
        .then(res => {
          demandes.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      disponibleDe: {},
      disponibleJusqua: {},
      quantite: {},
      annonce: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      citoyen: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      crise: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      demandeses: {},
    };
    const v$ = useVuelidate(validationRules, offre as any);
    v$.value.$validate();

    return {
      offreService,
      alertService,
      offre,
      previousState,
      isSaving,
      currentLanguage,
      annonces,
      citoyens,
      crises,
      demandes,
      v$,
      ...useDateFormat({ entityRef: offre }),
      t$,
    };
  },
  created(): void {
    this.offre.demandeses = [];
  },
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.offre.id) {
        this.offreService()
          .update(this.offre)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('assistaCriseApp.offre.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.offreService()
          .create(this.offre)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('assistaCriseApp.offre.created', { param: param.id }).toString());
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
