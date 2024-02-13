import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DashboardRoutingModule } from './dashboard-routing.module';
import { SharedModule } from '../shared/shared.module';
import { DashboardComponent } from './dashboard/dashboard.component';
import { PanelModule } from 'primeng/panel';
import { ChartModule } from 'primeng/chart';
import { DecimalPipe } from '@angular/common';

@NgModule({
  declarations: [DashboardComponent],
  imports: [
    CommonModule,
    SharedModule,
    PanelModule,
    ChartModule,
    DashboardRoutingModule,
  ],
  providers: [DecimalPipe],
})
export class DashboardModule {}
