import {Component, NgIterable, OnInit} from '@angular/core';
import {UtilisateurDto} from "../../../../../ds-api/src/models/utilisateur-dto";
import {UtilisateurService} from "../../../../../ds-api/src/services/utilisateur.service";
import {Router} from "@angular/router";

interface Mission {
  id: number;
  title: string;
  description: string;
  status: string;
  startDate: Date;
  endDate: Date;
  teamSize: number;
  progress: number;
}

interface User {
  id: number;
  fullname: string;
  email: string;
  phoneNum: string;
  role: string;
  enabled: boolean;
  accountLocked: boolean;
  dateOfBirth: Date;
  location: string;
  employeeId: string;
  joinDate: Date;
  department: string;
  position: string;
}

@Component({
  selector: 'app-my-profile',
  templateUrl: './my-profile.component.html',
  styleUrls: ['./my-profile.component.scss']
})
export class MyProfileComponent implements OnInit{


  missionFilter = 'all';

  user: User = {
    id: 1,
    fullname: 'John Doe',
    email: 'john.doe@example.com',
    phoneNum: '+1 (555) 123-4567',
    role: 'ADMIN',
    enabled: true,
    accountLocked: false,
    dateOfBirth: new Date('1990-05-15'),
    location: 'New York, USA',
    employeeId: 'EMP001',
    joinDate: new Date('2020-03-01'),
    department: 'Engineering',
    position: 'Senior Developer'
  };

  missions: Mission[] = [
    {
      id: 1,
      title: 'Project Alpha',
      description: 'Implement new authentication system',
      status: 'Active',
      startDate: new Date('2023-01-01'),
      endDate: new Date('2023-12-31'),
      teamSize: 5,
      progress: 75
    },
    {
      id: 2,
      title: 'Project Beta',
      description: 'Develop mobile application',
      status: 'Completed',
      startDate: new Date('2023-06-01'),
      endDate: new Date('2023-09-30'),
      teamSize: 8,
      progress: 100
    },
    {
      id: 3,
      title: 'Project Gamma',
      description: 'Database optimization',
      status: 'Pending',
      startDate: new Date('2023-11-01'),
      endDate: new Date('2024-02-28'),
      teamSize: 3,
      progress: 0
    }
  ];

  constructor(
    private router:Router,
    private userService:UtilisateurService) {
  }
getUserById(){
    this.userService.findById2({id:3}).subscribe(
      {
        next:(userRep)=>{
          this.user=userRep;
        },error:(err)=>{
          console.log(err);
        }
      }
    )
}
  ngOnInit(): void {
    this.getUserById();
  }


  getAvatarColor(name: string): string {
    const colors = [
      '#3498db', '#2ecc71', '#e74c3c', '#f1c40f', '#9b59b6',
      '#1abc9c', '#e67e22', '#34495e'
    ];
    const index = name.split('').reduce((acc, char) => acc + char.charCodeAt(0), 0);
    return colors[index % colors.length];
  }

  get filteredMissions(): Mission[] {
    if (this.missionFilter === 'all') return this.missions;
    return this.missions.filter(m => m.status.toLowerCase() === this.missionFilter);
  }

  getProgressColor(progress: number): string {
    if (progress >= 75) return 'success';
    if (progress >= 50) return 'primary';
    if (progress >= 25) return 'warning';
    return 'danger';
  }

  goBack(): void {
    this.router.navigate(['/users']);
  }

  editProfile(): void {
    // Implement edit profile navigation
    console.log('Edit profile clicked');
  }
}
