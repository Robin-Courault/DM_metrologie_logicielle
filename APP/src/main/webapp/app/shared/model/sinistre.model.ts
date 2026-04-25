import { type IUtilisateur } from '@/shared/model/utilisateur.model';

export interface ISinistre {
  id?: number;
  utilisateur?: IUtilisateur;
}

export class Sinistre implements ISinistre {
  constructor(
    public id?: number,
    public utilisateur?: IUtilisateur,
  ) {}
}
