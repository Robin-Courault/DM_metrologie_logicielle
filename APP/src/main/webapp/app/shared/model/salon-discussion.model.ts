import { type IUtilisateur } from '@/shared/model/utilisateur.model';

export interface ISalonDiscussion {
  id?: number;
  dateOuverture?: Date | null;
  ouvert?: boolean | null;
  participantses?: IUtilisateur[] | null;
}

export class SalonDiscussion implements ISalonDiscussion {
  constructor(
    public id?: number,
    public dateOuverture?: Date | null,
    public ouvert?: boolean | null,
    public participantses?: IUtilisateur[] | null,
  ) {
    this.ouvert = this.ouvert ?? false;
  }
}
