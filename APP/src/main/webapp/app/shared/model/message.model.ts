import { type ISalonDiscussion } from '@/shared/model/salon-discussion.model';
import { type IUtilisateur } from '@/shared/model/utilisateur.model';

export interface IMessage {
  id?: number;
  contenu?: string;
  dateEnvoi?: Date;
  utilisateur?: IUtilisateur;
  salonDiscussion?: ISalonDiscussion;
}

export class Message implements IMessage {
  constructor(
    public id?: number,
    public contenu?: string,
    public dateEnvoi?: Date,
    public utilisateur?: IUtilisateur,
    public salonDiscussion?: ISalonDiscussion,
  ) {}
}
