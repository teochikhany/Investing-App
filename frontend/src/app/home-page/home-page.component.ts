import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatTable } from '@angular/material/table';
import { Stock } from 'src/models/stock';
import { StockService } from 'src/services/stock.service';



@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css']
})
export class HomePageComponent implements AfterViewInit {

  @ViewChild(MatTable) stockList!: MatTable<any>;

  static table : MatTable<any>;
  displayedColumns: string[] = ['id', 'name', 'ticker', 'price'];

  constructor(private stockService: StockService) {}

  ngAfterViewInit(): void {
    this.getStocks();
    HomePageComponent.table = this.stockList;
  }

  getStocks(): void {
    this.stockService.getStocks()
        .subscribe(stocks => {this.stockService.setSource(stocks);} );
  }

  getList(): Stock[] {
    return this.stockService.getSource();
  }

}
