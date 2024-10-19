import {Component, OnInit} from '@angular/core';
import {CommandeClientDto} from "../../../../../../../ds-api/src/models/commande-client-dto";
import {CommandeClientService} from "../../../../../../../ds-api/src/services/commande-client.service";
import {CommandeClientResponse} from "../../../../../../../ds-api/src/models/commande-client-response";
import {
  PageResponseCommandeClientResponse
} from "../../../../../../../ds-api/src/models/page-response-commande-client-response";

@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.scss']
})
export class OrdersComponent implements OnInit{

  //order:CommandeClientDto={};
  allOrders: CommandeClientResponse[]=[];
  page: number = 0;
  size: number =5;
  allOrdersResponse: PageResponseCommandeClientResponse={};

  constructor(private cmdClientService:CommandeClientService) {
  }

  ngOnInit(): void {
    this.getAllOrders();

  }


  private getAllOrders() {
    this.cmdClientService.findAllCommandeClients({
      page:this.page,
      size:this.size
    }).subscribe({
      next:(orders)=>{
        console.log(orders);
        this.allOrdersResponse=orders;
      },
      error:(err)=>{
        console.log(err);
      }
    })
  }

  converteDateTime(dateCommnde: string | undefined):string {
    const dateTimeString = JSON.stringify(dateCommnde);
    const dateTimeToArray: number[] = JSON.parse(dateTimeString);

    return this.convertToDateTime(dateTimeToArray).toString();
  }

  private convertToDateTime(dateTimeToArray: number[]) :Date {
    const [year,month,day,hour,minute, second, nano]= dateTimeToArray;
    const date = new Date(year,month-1,day,hour,minute,second,nano/1000000);
    return date;

  }



  goToFirstPage() {
    this.page=0;
    this.getAllOrders();
  }

  goToPreviousPage() {
    this.page--;
    this.getAllOrders();
  }

  goToPage(pageIndex: number) {
    this.page=pageIndex;
    this.getAllOrders();
  }

  goToNextPage() {
    this.page++;
    this.getAllOrders();
  }


  get  isLastPage():boolean{
    return this.page == this.allOrdersResponse.totalPages as number -1;
  }

  goToLastPage() {
    this.page=this.allOrdersResponse.totalPages as number -1;
  }
}
