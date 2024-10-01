import { Component } from '@angular/core';
import {Menu} from './menu'
@Component({
  selector: 'app-side-bar-menu',
  templateUrl: './side-bar-menu.component.html',
  styleUrl: './side-bar-menu.component.scss'
})
export class SideBarMenuComponent  {

  public menuComponent: Array<Menu> =[{
    id: '1',
    maintitle:"Core",
    title: 'Dashboard',
    icon: '',
    url: '',
    subMenu:[]

  },
    {
      id:'2',
      maintitle:"INTERFACE",
      title:'Layouts',
      icon:'',
      url:'',
      subMenu:[
        {
          id: '21',
          maintitle:"",
          title: 'static navigation',
          icon: '',
          url: '',
        },
        {
          id: '22',
          maintitle:"",
          title: 'light sidenav',
          icon: '',
          url: '',
        }
      ]
    },
    {
      id:'3',
      maintitle:"Layouts",
      title:'Pages',
      icon:'',
      url:'',
      subMenu:[
        {
          id: '31',
          maintitle:"",
          title: 'subClient',
          icon: '',
          url: '',
        },
        {
          id: '32',
          maintitle:"",
          title: 'subClient',
          icon: '',
          url: '',
        },
        {
          id: '33',
          maintitle:"",
          title: 'subClient',
          icon: '',
          url: '',
        }
      ]
    },
    {
      id:'4',
      maintitle:"",
      title:'Client',
      icon:'',
      url:'',
      subMenu:[
        {
          id: '41',
          maintitle:"",
          title: 'subClient',
          icon: '',
          url: '',
        },
        {
          id: '42',
          maintitle:"",
          title: 'subClient',
          icon: '',
          url: '',
        },
        {
          id: '43',
          maintitle:"",
          title: 'subClient',
          icon: '',
          url: '',
        }
      ]
    },
    {
      id:'5',
      maintitle:"",
      title:'Client',
      icon:'',
      url:'',
      subMenu:[
        {
          id: '31',
          maintitle:"",
          title: 'subClient',
          icon: '',
          url: '',
        },
        {
          id: '32',
          maintitle:"",
          title: 'subClient',
          icon: '',
          url: '',
        },
        {
          id: '33',
          maintitle:"",
          title: 'subClient',
          icon: '',
          url: '',
        }
      ]
    },
    {
      id:'3',
      maintitle:"",
      title:'Utilisateur',
      icon:'',
      url:'',
      subMenu:[
        {
          id: '31',
          maintitle:"",
          title: 'subClient',
          icon: '',
          url: '',
        },
        {
          id: '32',
          maintitle:"",
          title: 'subClient',
          icon: '',
          url: '',
        },
        {
          id: '33',
          maintitle:"",
          title: 'subClient',
          icon: '',
          url: '',
        }
      ]
    }

];

}
