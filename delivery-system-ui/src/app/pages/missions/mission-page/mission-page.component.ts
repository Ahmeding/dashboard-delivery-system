import { Component } from '@angular/core';

@Component({
  selector: 'app-mission-page',
  templateUrl: './mission-page.component.html',
  styleUrl: './mission-page.component.scss'
})
export class MissionPageComponent {
  private etapedArray: Array<any> = [];
  private newAttribute: any = {};

  addFieldValue() {
    this.etapedArray.push(this.newAttribute)
    this.newAttribute = {};
  }
  //
  // deleteFieldValue(index) {
  //   this.etapedArray.splice(index, 1);
  // }
}
