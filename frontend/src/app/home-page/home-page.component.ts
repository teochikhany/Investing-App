import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatTable } from '@angular/material/table';
import { Stock } from 'src/models/stock';
import { StockService } from 'src/services/stock.service';
import { DomSanitizer } from '@angular/platform-browser';
import { MatIconRegistry } from '@angular/material/icon';



@Component({
    selector: 'app-home-page',
    templateUrl: './home-page.component.html',
    styleUrls: ['./home-page.component.css']
})
export class HomePageComponent implements AfterViewInit, OnInit {

    @ViewChild(MatTable) stockList!: MatTable<any>;

    static table: MatTable<any>;
    displayedColumns: string[] = ['id', 'name', 'ticker', 'price', 'action'];

    constructor(private stockService: StockService,
        private iconRegistry: MatIconRegistry,
        private sanitizer: DomSanitizer) { }

    ngOnInit(): void {
        this.iconRegistry.addSvgIcon(
            'trash',
            this.sanitizer.bypassSecurityTrustResourceUrl('assets/img/trash.svg'));
    }

    ngAfterViewInit(): void {
        this.getStocks();
        HomePageComponent.table = this.stockList;
    }

    getStocks(): void {
        this.stockService.getStocks()
    }

    getList(): Stock[] {
        return this.stockService.getSource();
    }

    deleteRow(id: number): void {
        this.stockService.deleteStock(id);
        HomePageComponent.table.renderRows();
    }

}
