import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import { Autorite, type IAutorite } from '@/shared/model/autorite.model';
import { TypeAutorite } from '@/shared/model/enumerations/type-autorite.model';

import AutoriteService from './autorite.service';

export default defineComponent({
  name: 'AutoriteUpdate',
  setup() {
    const autoriteService = inject('autoriteService', () => new AutoriteService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const autorite: Ref<IAutorite> = ref(new Autorite());
    const typeAutoriteValues: Ref<string[]> = ref(Object.keys(TypeAutorite));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'fr'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveAutorite = async autoriteId => {
      try {
        const res = await autoriteService().find(autoriteId);
        autorite.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.autoriteId) {
      retrieveAutorite(route.params.autoriteId);
    }

    const initRelationships = () => {};

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      nom: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      type: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      territoire: {},
      contact: {},
      criseses: {},
    };
    const v$ = useVuelidate(validationRules, autorite as any);
    v$.value.$validate();

    return {
      autoriteService,
      alertService,
      autorite,
      previousState,
      typeAutoriteValues,
      isSaving,
      currentLanguage,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.autorite.id) {
        this.autoriteService()
          .update(this.autorite)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('assistaCriseApp.autorite.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.autoriteService()
          .create(this.autorite)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('assistaCriseApp.autorite.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
