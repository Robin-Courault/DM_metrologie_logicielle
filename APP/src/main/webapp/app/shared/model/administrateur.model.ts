import { type IUtilisateur } from '@/shared/model/utilisateur.model';

export interface IAdministrateur {
  id?: number;
  utilisateur?: IUtilisateur;
}

export class Administrateur implements IAdministrateur {
  constructor(
    public id?: number,
    public utilisateur?: IUtilisateur,
  ) {}
}
