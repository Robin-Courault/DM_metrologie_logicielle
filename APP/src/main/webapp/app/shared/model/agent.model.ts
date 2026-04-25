import { type IAutorite } from '@/shared/model/autorite.model';
import { type IUtilisateur } from '@/shared/model/utilisateur.model';

export interface IAgent {
  id?: number;
  fonction?: string | null;
  service?: string | null;
  utilisateur?: IUtilisateur;
  autorite?: IAutorite;
}

export class Agent implements IAgent {
  constructor(
    public id?: number,
    public fonction?: string | null,
    public service?: string | null,
    public utilisateur?: IUtilisateur,
    public autorite?: IAutorite,
  ) {}
}
