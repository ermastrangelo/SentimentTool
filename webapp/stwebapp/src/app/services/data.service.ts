import { Injectable } from '@angular/core';
import { Http } from '@angular/http';

//import { HttpHeaders } from '@angular/common/http';//probando
import { Headers } from '@angular/http';

import { RequestOptionsArgs } from '@angular/http';

import 'rxjs/add/operator/map';//why???


const httpOptions = {
  headers: new Headers({
    //'Access-Control-Allow-Origin' : '*'
    'Access-Control-Allow-Origin' : 'http://localhost:8080'
  })
};


@Injectable()
export class DataService {

  constructor(public http: Http) { }
  
  getPost(){
	  //devuelve objeto del tipo observable, o sea tengo que definir uno que lo observe..suscribe
	  //el map es sobre el observable
	  //return this.http.get('https://jsonplaceholder.typicode.com/posts').map(res => res.json());
	  
	  //era el ultimo que me estabamos usando 
	  //return this.http.get('http://localhost:4848/single/?appid=C%3A%5CUsers%5CEric%5CDocuments%5CQlik%5CSense%5CApps%5Ctest.qvf&sheet=77fb839f-bc06-4380-805b-d49381fc51c7&opt=nointeraction&select=clearall',httpOptions).map(res => res.json());
	  
	  //llamada al back end
	  //console.log("LLAMAAA	"+action+user+id+cantBajar);
	  //return this.http.get('http://http://localhost:8080/websocket?action='+action+'&user='+user+'&tweetId='+id+'&cantBajar='+cantBajar,httpOptions).map(res => res.json()); 
	  
  }
  
 getBackEnd(action,user, id, cantBajar ) {
 
  console.log("Llamo a web socket controller");
  
  return this.http.get('http://localhost:8080/websocket?action='+action+'&user='+user+'&tweetId='+id+'&cantBajar='+cantBajar).map(res => res.json());//.catch(this.handleError);
}

  
  

		  

}
