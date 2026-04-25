import { type CategorieBesoin } from '@/shared/model/enumerations/categorie-besoin.model';
import { type EtatAnnonce } from '@/shared/model/enumerations/etat-annonce.model';
export interface IAnnonce {
  id?: number;
  titre?: string;
  description?: string | null;
  categorie?: keyof typeof CategorieBesoin;
  latitude?: number | null;
  longitude?: number | null;
  adresse?: string | null;
  dateCreation?: Date | null;
  dateMaJ?: Date | null;
  etat?: keyof typeof EtatAnnonce | null;
}

export class Annonce implements IAnnonce {
  constructor(
    public id?: number,
    public titre?: string,
    public description?: string | null,
    public categorie?: keyof typeof CategorieBesoin,
    public latitude?: number | null,
    public longitude?: number | null,
    public adresse?: string | null,
    public dateCreation?: Date | null,
    public dateMaJ?: Date | null,
    public etat?: keyof typeof EtatAnnonce | null,
  ) {}
}
