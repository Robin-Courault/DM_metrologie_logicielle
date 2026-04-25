import { Authority } from '@/shared/jhipster/constants';
const Entities = () => import('@/entities/entities.vue');

const Utilisateur = () => import('@/entities/utilisateur/utilisateur.vue');
const UtilisateurUpdate = () => import('@/entities/utilisateur/utilisateur-update.vue');
const UtilisateurDetails = () => import('@/entities/utilisateur/utilisateur-details.vue');

const Sinistre = () => import('@/entities/sinistre/sinistre.vue');
const SinistreUpdate = () => import('@/entities/sinistre/sinistre-update.vue');
const SinistreDetails = () => import('@/entities/sinistre/sinistre-details.vue');

const Citoyen = () => import('@/entities/citoyen/citoyen.vue');
const CitoyenUpdate = () => import('@/entities/citoyen/citoyen-update.vue');
const CitoyenDetails = () => import('@/entities/citoyen/citoyen-details.vue');

const Agent = () => import('@/entities/agent/agent.vue');
const AgentUpdate = () => import('@/entities/agent/agent-update.vue');
const AgentDetails = () => import('@/entities/agent/agent-details.vue');

const Administrateur = () => import('@/entities/administrateur/administrateur.vue');
const AdministrateurUpdate = () => import('@/entities/administrateur/administrateur-update.vue');
const AdministrateurDetails = () => import('@/entities/administrateur/administrateur-details.vue');

const Autorite = () => import('@/entities/autorite/autorite.vue');
const AutoriteUpdate = () => import('@/entities/autorite/autorite-update.vue');
const AutoriteDetails = () => import('@/entities/autorite/autorite-details.vue');

const Crise = () => import('@/entities/crise/crise.vue');
const CriseUpdate = () => import('@/entities/crise/crise-update.vue');
const CriseDetails = () => import('@/entities/crise/crise-details.vue');

const Annonce = () => import('@/entities/annonce/annonce.vue');
const AnnonceUpdate = () => import('@/entities/annonce/annonce-update.vue');
const AnnonceDetails = () => import('@/entities/annonce/annonce-details.vue');

const Demande = () => import('@/entities/demande/demande.vue');
const DemandeUpdate = () => import('@/entities/demande/demande-update.vue');
const DemandeDetails = () => import('@/entities/demande/demande-details.vue');

const Offre = () => import('@/entities/offre/offre.vue');
const OffreUpdate = () => import('@/entities/offre/offre-update.vue');
const OffreDetails = () => import('@/entities/offre/offre-details.vue');

const SalonDiscussion = () => import('@/entities/salon-discussion/salon-discussion.vue');
const SalonDiscussionUpdate = () => import('@/entities/salon-discussion/salon-discussion-update.vue');
const SalonDiscussionDetails = () => import('@/entities/salon-discussion/salon-discussion-details.vue');

const Message = () => import('@/entities/message/message.vue');
const MessageUpdate = () => import('@/entities/message/message-update.vue');
const MessageDetails = () => import('@/entities/message/message-details.vue');

const ModerationAction = () => import('@/entities/moderation-action/moderation-action.vue');
const ModerationActionUpdate = () => import('@/entities/moderation-action/moderation-action-update.vue');
const ModerationActionDetails = () => import('@/entities/moderation-action/moderation-action-details.vue');

// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'utilisateur',
      name: 'Utilisateur',
      component: Utilisateur,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'utilisateur/new',
      name: 'UtilisateurCreate',
      component: UtilisateurUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'utilisateur/:utilisateurId/edit',
      name: 'UtilisateurEdit',
      component: UtilisateurUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'utilisateur/:utilisateurId/view',
      name: 'UtilisateurView',
      component: UtilisateurDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'sinistre',
      name: 'Sinistre',
      component: Sinistre,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'sinistre/new',
      name: 'SinistreCreate',
      component: SinistreUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'sinistre/:sinistreId/edit',
      name: 'SinistreEdit',
      component: SinistreUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'sinistre/:sinistreId/view',
      name: 'SinistreView',
      component: SinistreDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'citoyen',
      name: 'Citoyen',
      component: Citoyen,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'citoyen/new',
      name: 'CitoyenCreate',
      component: CitoyenUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'citoyen/:citoyenId/edit',
      name: 'CitoyenEdit',
      component: CitoyenUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'citoyen/:citoyenId/view',
      name: 'CitoyenView',
      component: CitoyenDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'agent',
      name: 'Agent',
      component: Agent,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'agent/new',
      name: 'AgentCreate',
      component: AgentUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'agent/:agentId/edit',
      name: 'AgentEdit',
      component: AgentUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'agent/:agentId/view',
      name: 'AgentView',
      component: AgentDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'administrateur',
      name: 'Administrateur',
      component: Administrateur,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'administrateur/new',
      name: 'AdministrateurCreate',
      component: AdministrateurUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'administrateur/:administrateurId/edit',
      name: 'AdministrateurEdit',
      component: AdministrateurUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'administrateur/:administrateurId/view',
      name: 'AdministrateurView',
      component: AdministrateurDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'autorite',
      name: 'Autorite',
      component: Autorite,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'autorite/new',
      name: 'AutoriteCreate',
      component: AutoriteUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'autorite/:autoriteId/edit',
      name: 'AutoriteEdit',
      component: AutoriteUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'autorite/:autoriteId/view',
      name: 'AutoriteView',
      component: AutoriteDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'crise',
      name: 'Crise',
      component: Crise,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'crise/new',
      name: 'CriseCreate',
      component: CriseUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'crise/:criseId/edit',
      name: 'CriseEdit',
      component: CriseUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'crise/:criseId/view',
      name: 'CriseView',
      component: CriseDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'annonce',
      name: 'Annonce',
      component: Annonce,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'annonce/new',
      name: 'AnnonceCreate',
      component: AnnonceUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'annonce/:annonceId/edit',
      name: 'AnnonceEdit',
      component: AnnonceUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'annonce/:annonceId/view',
      name: 'AnnonceView',
      component: AnnonceDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'demande',
      name: 'Demande',
      component: Demande,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'demande/new',
      name: 'DemandeCreate',
      component: DemandeUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'demande/:demandeId/edit',
      name: 'DemandeEdit',
      component: DemandeUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'demande/:demandeId/view',
      name: 'DemandeView',
      component: DemandeDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'offre',
      name: 'Offre',
      component: Offre,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'offre/new',
      name: 'OffreCreate',
      component: OffreUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'offre/:offreId/edit',
      name: 'OffreEdit',
      component: OffreUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'offre/:offreId/view',
      name: 'OffreView',
      component: OffreDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'salon-discussion',
      name: 'SalonDiscussion',
      component: SalonDiscussion,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'salon-discussion/new',
      name: 'SalonDiscussionCreate',
      component: SalonDiscussionUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'salon-discussion/:salonDiscussionId/edit',
      name: 'SalonDiscussionEdit',
      component: SalonDiscussionUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'salon-discussion/:salonDiscussionId/view',
      name: 'SalonDiscussionView',
      component: SalonDiscussionDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'message',
      name: 'Message',
      component: Message,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'message/new',
      name: 'MessageCreate',
      component: MessageUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'message/:messageId/edit',
      name: 'MessageEdit',
      component: MessageUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'message/:messageId/view',
      name: 'MessageView',
      component: MessageDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'moderation-action',
      name: 'ModerationAction',
      component: ModerationAction,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'moderation-action/new',
      name: 'ModerationActionCreate',
      component: ModerationActionUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'moderation-action/:moderationActionId/edit',
      name: 'ModerationActionEdit',
      component: ModerationActionUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'moderation-action/:moderationActionId/view',
      name: 'ModerationActionView',
      component: ModerationActionDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};
