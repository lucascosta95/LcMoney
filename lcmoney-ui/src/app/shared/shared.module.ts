import { MessageComponent } from './message/message.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';



@NgModule({
  declarations: [MessageComponent],
  imports: [
    CommonModule
  ],
  exports: [MessageComponent]
})
export class SharedModule { }
