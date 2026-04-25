import { type IAnnonce } from '@/shared/model/annonce.model';
import { type ICitoyen } from '@/shared/model/citoyen.model';
import { type ICrise } from '@/shared/model/crise.model';
import { type IDemande } from '@/shared/model/demande.model';

export interface IOffre {
  id?: number;
  disponibleDe?: Date | null;
  disponibleJusqua?: Date | null;
  quantite?: number | null;
  annonce?: IAnnonce;
  citoyen?: ICitoyen;
  crise?: ICrise;
  demandeses?: IDemande[] | null;
}

export class Offre implements IOffre {
  constructor(
    public id?: number,
    public disponibleDe?: Date | null,
    public disponibleJusqua?: Date | null,
    public quantite?: number | null,
    public annonce?: IAnnonce,
    public citoyen?: ICitoyen,
    public crise?: ICrise,
    public demandeses?: IDemande[] | null,
  ) {}
}
