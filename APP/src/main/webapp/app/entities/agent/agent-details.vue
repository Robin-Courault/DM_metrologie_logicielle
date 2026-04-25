<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="agent">
        <h2 class="jh-entity-heading" data-cy="agentDetailsHeading">
          <span>{{ t$('assistaCriseApp.agent.detail.title') }}Agent</span> {{ agent.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ t$('assistaCriseApp.agent.fonction') }}</span>
          </dt>
          <dd>
            <span>{{ agent.fonction }}</span>
          </dd>
          <dt>
            <span>{{ t$('assistaCriseApp.agent.service') }}</span>
          </dt>
          <dd>
            <span>{{ agent.service }}</span>
          </dd>
          <dt>
            <span>{{ t$('assistaCriseApp.agent.utilisateur') }}</span>
          </dt>
          <dd>
            <div v-if="agent.utilisateur">
              <router-link :to="{ name: 'UtilisateurView', params: { utilisateurId: agent.utilisateur.id } }">{{
                agent.utilisateur.login
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>{{ t$('assistaCriseApp.agent.autorite') }}</span>
          </dt>
          <dd>
            <div v-if="agent.autorite">
              <router-link :to="{ name: 'AutoriteView', params: { autoriteId: agent.autorite.id } }">{{ agent.autorite.nom }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ t$('entity.action.back') }}</span>
        </button>
        <router-link v-if="agent.id" :to="{ name: 'AgentEdit', params: { agentId: agent.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>{{ t$('entity.action.edit') }}</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./agent-details.component.ts"></script>
