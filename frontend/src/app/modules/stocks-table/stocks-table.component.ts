import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatTable } from '@angular/material/table';
import { Stock } from 'src/app/models/stock';
import { StockService } from 'src/app/services/stock.service';
import { DomSanitizer } from '@angular/platform-browser';
import { MatIconRegistry } from '@angular/material/icon';



@Component({
    selector: 'stocks-table-page',
    templateUrl: './stocks-table.component.html',
    styleUrls: ['./stocks-table.component.css']
})
export class StockTableComponent implements AfterViewInit, OnInit {

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
        StockTableComponent.table = this.stockList;
    }

    getStocks(): void {
        this.stockService.getStocks()
    }

    getList(): Stock[] {
        return this.stockService.getSource();
    }

    deleteRow(id: number): void {
        this.stockService.deleteStock(id, StockTableComponent.table);
    }

}
