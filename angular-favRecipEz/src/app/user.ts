export class User{
    id:number;
    userName:string;
    token:string;
    email:string;
    roles: RoleName[];
    
    
}

export type RoleName = "ROLE_USER" | "ROLE_ADMIN";