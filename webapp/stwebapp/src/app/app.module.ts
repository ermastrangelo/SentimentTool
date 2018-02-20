import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';


import { AppComponent } from './app.component';


@NgModule({
  declarations: [//componentes que ya construí
    AppComponent
  ],
  imports: [//agrego modulos que necesito, sumar http
    BrowserModule
  ],
  providers: [],//servicios, es un componente especial, es inyectable...lo puedo usar everywere
  				//de aca voy a llamar al backend
  bootstrap: [AppComponent]
})
export class AppModule { }
