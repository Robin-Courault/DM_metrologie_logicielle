import { type TypeAutorite } from '@/shared/model/enumerations/type-autorite.model';
export interface IAutorite {
  id?: number;
  nom?: string;
  type?: keyof typeof TypeAutorite;
  territoire?: string | null;
  contact?: string | null;
}

export class Autorite implements IAutorite {
  constructor(
    public id?: number,
    public nom?: string,
    public type?: keyof typeof TypeAutorite,
    public territoire?: string | null,
    public contact?: string | null,
  ) {}
}
