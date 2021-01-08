import { Component, OnInit } from '@angular/core';
import { TokenStorageService } from '../token-storage.service';
import { User } from '../user';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  editProfileInfo: boolean = false;
  editProfileIcon: boolean = true;
  user: User; 
  constructor(private tokenStorServ: TokenStorageService) { }

  ngOnInit(): void {
      this.user = this.tokenStorServ.getUserData();
  }

  editProfile(): void{
    this.editProfileIcon = false;
    this.editProfileInfo = true;
  }

  closeEdit(): void{
    this.editProfileIcon = true;
    this.editProfileInfo = false;
  }

}
