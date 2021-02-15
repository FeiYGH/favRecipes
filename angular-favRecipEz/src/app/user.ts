export class User{
    id:number;
    userName:string;
    token:string;
    email:string;
    roles: RoleName[];
    profile_pic_url:string;
    
}

export type RoleName = "ROLE_USER" | "ROLE_ADMIN";