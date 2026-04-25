import { type IUtilisateur } from '@/shared/model/utilisateur.model';

export interface ICitoyen {
  id?: number;
  utilisateur?: IUtilisateur;
}

export class Citoyen implements ICitoyen {
  constructor(
    public id?: number,
    public utilisateur?: IUtilisateur,
  ) {}
}
