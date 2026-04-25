import { type IAutorite } from '@/shared/model/autorite.model';
import { type TypeCrise } from '@/shared/model/enumerations/type-crise.model';
export interface ICrise {
  id?: number;
  titre?: string;
  description?: string | null;
  type?: keyof typeof TypeCrise;
  dateDebut?: Date;
  dateFin?: Date | null;
  zoneGeographique?: string | null;
  cloturee?: boolean | null;
  autorite?: IAutorite;
}

export class Crise implements ICrise {
  constructor(
    public id?: number,
    public titre?: string,
    public description?: string | null,
    public type?: keyof typeof TypeCrise,
    public dateDebut?: Date,
    public dateFin?: Date | null,
    public zoneGeographique?: string | null,
    public cloturee?: boolean | null,
    public autorite?: IAutorite,
  ) {
    this.cloturee = this.cloturee ?? false;
  }
}
