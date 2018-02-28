import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DataService } from './services/data.service';
import { HttpModule } from '@angular/http';

import { AppComponent } from './app.component';
import { UserComponent } from './components/user/user.component';


@NgModule({
  declarations: [//componentes que ya construí
    AppComponent, UserComponent
  ],
  imports: [//agrego modulos que necesito, sumar http
    BrowserModule,
    FormsModule,
    HttpModule
  ],
  providers: [
	DataService          
  ],			//servicios, es un componente especial, es inyectable...lo puedo usar everywere
  				//de aca voy a llamar al backend
  bootstrap: [AppComponent]
})
export class AppModule { }
