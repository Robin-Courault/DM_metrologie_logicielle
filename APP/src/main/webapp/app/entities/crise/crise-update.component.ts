import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import AutoriteService from '@/entities/autorite/autorite.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import { type IAutorite } from '@/shared/model/autorite.model';
import { Crise, type ICrise } from '@/shared/model/crise.model';
import { TypeCrise } from '@/shared/model/enumerations/type-crise.model';

import CriseService from './crise.service';

export default defineComponent({
  name: 'CriseUpdate',
  setup() {
    const criseService = inject('criseService', () => new CriseService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const crise: Ref<ICrise> = ref(new Crise());

    const autoriteService = inject('autoriteService', () => new AutoriteService());

    const autorites: Ref<IAutorite[]> = ref([]);
    const typeCriseValues: Ref<string[]> = ref(Object.keys(TypeCrise));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'fr'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveCrise = async criseId => {
      try {
        const res = await criseService().find(criseId);
        res.dateDebut = new Date(res.dateDebut);
        res.dateFin = new Date(res.dateFin);
        crise.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.criseId) {
      retrieveCrise(route.params.criseId);
    }

    const initRelationships = () => {
      autoriteService()
        .retrieve()
        .then(res => {
          autorites.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      titre: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      description: {},
      type: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      dateDebut: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      dateFin: {},
      zoneGeographique: {},
      cloturee: {},
      autorite: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
    };
    const v$ = useVuelidate(validationRules, crise as any);
    v$.value.$validate();

    return {
      criseService,
      alertService,
      crise,
      previousState,
      typeCriseValues,
      isSaving,
      currentLanguage,
      autorites,
      v$,
      ...useDateFormat({ entityRef: crise }),
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.crise.id) {
        this.criseService()
          .update(this.crise)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('assistaCriseApp.crise.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.criseService()
          .create(this.crise)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('assistaCriseApp.crise.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
