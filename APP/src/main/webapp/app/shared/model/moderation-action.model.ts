import { type IAdministrateur } from '@/shared/model/administrateur.model';
import { type IAnnonce } from '@/shared/model/annonce.model';
import { type TypeModeration } from '@/shared/model/enumerations/type-moderation.model';
import { type IUtilisateur } from '@/shared/model/utilisateur.model';

export interface IModerationAction {
  id?: number;
  dateAction?: Date;
  motif?: string | null;
  type?: keyof typeof TypeModeration;
  administrateur?: IAdministrateur;
  annonce?: IAnnonce | null;
  utilisateurCible?: IUtilisateur | null;
}

export class ModerationAction implements IModerationAction {
  constructor(
    public id?: number,
    public dateAction?: Date,
    public motif?: string | null,
    public type?: keyof typeof TypeModeration,
    public administrateur?: IAdministrateur,
    public annonce?: IAnnonce | null,
    public utilisateurCible?: IUtilisateur | null,
  ) {}
}
