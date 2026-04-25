<template>
  <div>
    <b-dropdown-item to="/utilisateur">
      <font-awesome-icon icon="asterisk" />
      <span>{{ t$('global.menu.entities.utilisateur') }}</span>
    </b-dropdown-item>
    <b-dropdown-item to="/sinistre">
      <font-awesome-icon icon="asterisk" />
      <span>{{ t$('global.menu.entities.sinistre') }}</span>
    </b-dropdown-item>
    <b-dropdown-item to="/citoyen">
      <font-awesome-icon icon="asterisk" />
      <span>{{ t$('global.menu.entities.citoyen') }}</span>
    </b-dropdown-item>
    <b-dropdown-item to="/agent">
      <font-awesome-icon icon="asterisk" />
      <span>{{ t$('global.menu.entities.agent') }}</span>
    </b-dropdown-item>
    <b-dropdown-item to="/administrateur">
      <font-awesome-icon icon="asterisk" />
      <span>{{ t$('global.menu.entities.administrateur') }}</span>
    </b-dropdown-item>
    <b-dropdown-item to="/autorite">
      <font-awesome-icon icon="asterisk" />
      <span>{{ t$('global.menu.entities.autorite') }}</span>
    </b-dropdown-item>
    <b-dropdown-item to="/crise">
      <font-awesome-icon icon="asterisk" />
      <span>{{ t$('global.menu.entities.crise') }}</span>
    </b-dropdown-item>
    <b-dropdown-item to="/annonce">
      <font-awesome-icon icon="asterisk" />
      <span>{{ t$('global.menu.entities.annonce') }}</span>
    </b-dropdown-item>
    <b-dropdown-item to="/demande">
      <font-awesome-icon icon="asterisk" />
      <span>{{ t$('global.menu.entities.demande') }}</span>
    </b-dropdown-item>
    <b-dropdown-item to="/offre">
      <font-awesome-icon icon="asterisk" />
      <span>{{ t$('global.menu.entities.offre') }}</span>
    </b-dropdown-item>
    <b-dropdown-item to="/salon-discussion">
      <font-awesome-icon icon="asterisk" />
      <span>{{ t$('global.menu.entities.salonDiscussion') }}</span>
    </b-dropdown-item>
    <b-dropdown-item to="/message">
      <font-awesome-icon icon="asterisk" />
      <span>{{ t$('global.menu.entities.message') }}</span>
    </b-dropdown-item>
    <b-dropdown-item to="/moderation-action">
      <font-awesome-icon icon="asterisk" />
      <span>{{ t$('global.menu.entities.moderationAction') }}</span>
    </b-dropdown-item>
    <!-- jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here -->
  </div>
</template>

<script lang="ts" src="./entities-menu.component.ts"></script>
