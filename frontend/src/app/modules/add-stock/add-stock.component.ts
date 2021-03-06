import { Component, ViewChild } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { Stock } from 'src/app/models/stock';
import { StockService } from 'src/app/services/stock.service';
import { StockTableComponent } from '../stocks-table/stocks-table.component';



@Component({
    selector: 'app-add-stock',
    templateUrl: './add-stock.component.html',
    styleUrls: ['./add-stock.component.css']
})
export class AddStockComponent {

    constructor(public dialog: MatDialog) { }

    openDialog(): void {
        this.dialog.open(AppAddDialoge, { width: '25%' });
    }

}

@Component({
    selector: 'app-add-stock-dialog',
    templateUrl: 'add-stock-dialoge.html',
    styleUrls: ['./add-stock.component.css']
})
export class AppAddDialoge {

    new_stock: Stock = {
        id: 0,
        name: '',
        ticker: '',
        price: 0
    };

    constructor(
        private dialogRef: MatDialogRef<AppAddDialoge>,
        private stockService: StockService
    ) { }

    onNoClick(): void {
        this.dialogRef.close();
    }

    addStockClick(): void {
        this.stockService.postStock(this.new_stock, StockTableComponent.table);
        this.dialogRef.close();
    }

}
