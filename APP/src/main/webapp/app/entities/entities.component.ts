import { defineComponent, provide } from 'vue';

import UserService from '@/entities/user/user.service';

import AdministrateurService from './administrateur/administrateur.service';
import AgentService from './agent/agent.service';
import AnnonceService from './annonce/annonce.service';
import AutoriteService from './autorite/autorite.service';
import CitoyenService from './citoyen/citoyen.service';
import CriseService from './crise/crise.service';
import DemandeService from './demande/demande.service';
import MessageService from './message/message.service';
import ModerationActionService from './moderation-action/moderation-action.service';
import OffreService from './offre/offre.service';
import SalonDiscussionService from './salon-discussion/salon-discussion.service';
import SinistreService from './sinistre/sinistre.service';
import UtilisateurService from './utilisateur/utilisateur.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

export default defineComponent({
  name: 'Entities',
  setup() {
    provide('userService', () => new UserService());
    provide('utilisateurService', () => new UtilisateurService());
    provide('sinistreService', () => new SinistreService());
    provide('citoyenService', () => new CitoyenService());
    provide('agentService', () => new AgentService());
    provide('administrateurService', () => new AdministrateurService());
    provide('autoriteService', () => new AutoriteService());
    provide('criseService', () => new CriseService());
    provide('annonceService', () => new AnnonceService());
    provide('demandeService', () => new DemandeService());
    provide('offreService', () => new OffreService());
    provide('salonDiscussionService', () => new SalonDiscussionService());
    provide('messageService', () => new MessageService());
    provide('moderationActionService', () => new ModerationActionService());
    // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
  },
});
