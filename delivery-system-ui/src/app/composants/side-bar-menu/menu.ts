export interface Menu{
  id?:string,
  maintitle:string,
  title?:string,
  icon?:string,
  url?:string,
  subMenu?: Array<Menu>,
}
