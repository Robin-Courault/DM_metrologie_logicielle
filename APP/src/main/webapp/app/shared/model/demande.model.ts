import { type IAnnonce } from '@/shared/model/annonce.model';
import { type ICrise } from '@/shared/model/crise.model';
import { type EtatDemande } from '@/shared/model/enumerations/etat-demande.model';
import { type IOffre } from '@/shared/model/offre.model';
import { type ISalonDiscussion } from '@/shared/model/salon-discussion.model';
import { type ISinistre } from '@/shared/model/sinistre.model';
export interface IDemande {
  id?: number;
  etatDemande?: keyof typeof EtatDemande | null;
  dateFermeture?: Date | null;
  quantite?: number | null;
  annonce?: IAnnonce;
  salonDiscussion?: ISalonDiscussion | null;
  sinistre?: ISinistre;
  crise?: ICrise;
  offreses?: IOffre[] | null;
}

export class Demande implements IDemande {
  constructor(
    public id?: number,
    public etatDemande?: keyof typeof EtatDemande | null,
    public dateFermeture?: Date | null,
    public quantite?: number | null,
    public annonce?: IAnnonce,
    public salonDiscussion?: ISalonDiscussion | null,
    public sinistre?: ISinistre,
    public crise?: ICrise,
    public offreses?: IOffre[] | null,
  ) {}
}
