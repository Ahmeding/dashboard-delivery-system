import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {PageResponseUserResponse} from "../../../../../ds-api/src/models/page-response-user-response";
import {UserResponse} from "../../../../../ds-api/src/models/user-response";
import {UserService} from "../../../../../ds-api/src/services/user.service";

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.scss']
})
export class UsersComponent implements OnInit{
  allUsersResponse: PageResponseUserResponse={};
  selectedUser: UserResponse={};
  page:number=0;
  size:number=5;
  pageSize: any;
  searchTerm: any;

  constructor(private router:Router,
              private userService:UserService) {
  }

  ngOnInit(): void {
    this.getAllUsers();
  }

  getAllUsers(){
    this.userService.findAllUser({
      page:this.page,
      size:this.size
    }).subscribe({
      next:(userRep)=>{
      this.allUsersResponse=userRep;
    },error:(err)=>{
        console.log(err);
      }
    })
  }
  navigateToNewUser() {
    this.router.navigate(['settings','new-user'])
  }





  goToPreviousPage() {
    this.page--;
    this.getAllUsers();
  }

  goToPage(pageIndex: number) {
    this.page=pageIndex;
    this.getAllUsers();
  }

  goToNextPage() {
    this.page++;
    this.getAllUsers();
  }


  get  isLastPage():boolean{
    return this.page == this.allUsersResponse.totalPages as number -1;
  }

  goToLastPage() {
    this.page=this.allUsersResponse.totalPages as number -1;
  }

  goToFirstPage() {
    this.page=0;
    this.getAllUsers();
  }

  editUser(number: number) {

  }

  deleteUser(number: number) {

  }

  onPageSizeChange() {

  }

  filterUsers() {

  }
}
