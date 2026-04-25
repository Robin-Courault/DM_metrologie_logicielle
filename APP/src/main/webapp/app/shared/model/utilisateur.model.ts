import { type ISalonDiscussion } from '@/shared/model/salon-discussion.model';

export interface IUtilisateur {
  id?: number;
  login?: string;
  nom?: string;
  prenom?: string;
  email?: string;
  telephone?: string | null;
  motDePasse?: string;
  dateInscription?: Date | null;
  actif?: boolean | null;
  banni?: boolean | null;
  salonses?: ISalonDiscussion[] | null;
}

export class Utilisateur implements IUtilisateur {
  constructor(
    public id?: number,
    public login?: string,
    public nom?: string,
    public prenom?: string,
    public email?: string,
    public telephone?: string | null,
    public motDePasse?: string,
    public dateInscription?: Date | null,
    public actif?: boolean | null,
    public banni?: boolean | null,
    public salonses?: ISalonDiscussion[] | null,
  ) {
    this.actif = this.actif ?? false;
    this.banni = this.banni ?? false;
  }
}
