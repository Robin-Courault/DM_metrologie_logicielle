import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import AnnonceService from '@/entities/annonce/annonce.service';
import CriseService from '@/entities/crise/crise.service';
import OffreService from '@/entities/offre/offre.service';
import SalonDiscussionService from '@/entities/salon-discussion/salon-discussion.service';
import SinistreService from '@/entities/sinistre/sinistre.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import { type IAnnonce } from '@/shared/model/annonce.model';
import { type ICrise } from '@/shared/model/crise.model';
import { Demande, type IDemande } from '@/shared/model/demande.model';
import { EtatDemande } from '@/shared/model/enumerations/etat-demande.model';
import { type IOffre } from '@/shared/model/offre.model';
import { type ISalonDiscussion } from '@/shared/model/salon-discussion.model';
import { type ISinistre } from '@/shared/model/sinistre.model';

import DemandeService from './demande.service';

export default defineComponent({
  name: 'DemandeUpdate',
  setup() {
    const demandeService = inject('demandeService', () => new DemandeService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const demande: Ref<IDemande> = ref(new Demande());

    const annonceService = inject('annonceService', () => new AnnonceService());

    const annonces: Ref<IAnnonce[]> = ref([]);

    const salonDiscussionService = inject('salonDiscussionService', () => new SalonDiscussionService());

    const salonDiscussions: Ref<ISalonDiscussion[]> = ref([]);

    const sinistreService = inject('sinistreService', () => new SinistreService());

    const sinistres: Ref<ISinistre[]> = ref([]);

    const criseService = inject('criseService', () => new CriseService());

    const crises: Ref<ICrise[]> = ref([]);

    const offreService = inject('offreService', () => new OffreService());

    const offres: Ref<IOffre[]> = ref([]);
    const etatDemandeValues: Ref<string[]> = ref(Object.keys(EtatDemande));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'fr'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveDemande = async demandeId => {
      try {
        const res = await demandeService().find(demandeId);
        res.dateFermeture = new Date(res.dateFermeture);
        demande.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.demandeId) {
      retrieveDemande(route.params.demandeId);
    }

    const initRelationships = () => {
      annonceService()
        .retrieve()
        .then(res => {
          annonces.value = res.data;
        });
      salonDiscussionService()
        .retrieve()
        .then(res => {
          salonDiscussions.value = res.data;
        });
      sinistreService()
        .retrieve()
        .then(res => {
          sinistres.value = res.data;
        });
      criseService()
        .retrieve()
        .then(res => {
          crises.value = res.data;
        });
      offreService()
        .retrieve()
        .then(res => {
          offres.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      etatDemande: {},
      dateFermeture: {},
      quantite: {},
      annonce: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      salonDiscussion: {},
      sinistre: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      crise: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      offreses: {},
    };
    const v$ = useVuelidate(validationRules, demande as any);
    v$.value.$validate();

    return {
      demandeService,
      alertService,
      demande,
      previousState,
      etatDemandeValues,
      isSaving,
      currentLanguage,
      annonces,
      salonDiscussions,
      sinistres,
      crises,
      offres,
      v$,
      ...useDateFormat({ entityRef: demande }),
      t$,
    };
  },
  created(): void {
    this.demande.offreses = [];
  },
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.demande.id) {
        this.demandeService()
          .update(this.demande)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('assistaCriseApp.demande.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.demandeService()
          .create(this.demande)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('assistaCriseApp.demande.created', { param: param.id }).toString());
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
