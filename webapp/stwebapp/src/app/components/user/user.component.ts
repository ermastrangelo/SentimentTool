import { Component, OnInit } from '@angular/core';
import { DataService } from '../../services/data.service';


@Component({
  selector: 'app-user', //a todo lo que tenga esta etiqueta, le mete el html de abajo 
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css'] //despues vemos que le metemos con bootstraṕ
})


//todos los componentes tiene que tener un OnInit para que funcionen
export class UserComponent implements OnInit { //declaro todo esto como una clase(componente)

  name: string ;//string interpolation, la ve desde el html {{name}}
  age: number;
  email: string;
  address : Address;
  hobbies: string[];
  posts: Post[];
  	

  constructor(private dataService: DataService) { 
	    
  	console.log("constructor: Running"); 
  	
  	
  }

  ngOnInit() {
	 console.log("OnInit: Running ");
    this.age=36;
  	this.name='Eric';
  	this.email= 'ericormnmastrangelo@gmail.com'
    this.address = {
    		   	street: 'Richieri 327',
    		   	city: 'Tandil',
    		   	state: 'BA'
       };
  	this.hobbies=['dance', 'eat', 'codean'];
  	
  	//me suscribo al servicio (observanle), y le pido que me meta en posts el resultado
  	this.dataService.getPost().subscribe((posts) =>   {console.log(posts);
  														this.posts=posts; 
  														} );
  	
  	 	 	
  	
  } 
  
  onClick(){
	  this.name="Roman";
	  
  }
  
  addHobby(hobby){
	  this.hobbies.unshift(hobby);
	  return false;
}
  
  deleteHobby(hobby){
	  for(let i= 0; i<this.hobbies.length; i++){
		  if(this.hobbies[i] == hobby){
			  this.hobbies.splice(i, 1 );
		  }
	  }
  }  

}

interface Post {
	  id: number;
	  title: string;
	  body: string;
	  userId: number;
	}

interface Address{
	street: string;
    		city: string;
    		state: string;
}
