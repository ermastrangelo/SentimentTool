import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import 'rxjs/add/operator/map';

@Injectable()
export class DataService {

  constructor(public http: Http) { }
  
  getPost(){
	  //devuelve objeto del tipo observable, o sea tengo que definir uno que lo observe..suscribe
	  //el map es sobre el observable
	  return this.http.get('https://jsonplaceholder.typicode.com/posts').map(res => res.json()); 
	  
  }
		  

}
