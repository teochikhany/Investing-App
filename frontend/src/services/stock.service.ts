import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Stock } from '../models/stock';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatTable } from '@angular/material/table';


@Injectable({
    providedIn: 'root'
})
export class StockService {

    private stocksUrl = 'http://localhost:8080/api/v1/stocks/';  // URL to web api
    private dataSource: Stock[] = [];

    constructor(private http: HttpClient, private snackBar: MatSnackBar) { }

    getSource(): Stock[] {
        return this.dataSource
    }

    private setSource(stocks: Stock[]): void {
        this.dataSource = stocks;
    }

    getStocks(): void {
        this.http.get<Stock[]>(this.stocksUrl, {observe: 'response'})
        .subscribe(
            response => {
                if (response.status == 200) {
                    this.setSource(response.body!!);
                }
                else {
                    this.snackBar.open("An Error Occurred", "Dismiss");
                }
            },
            err => {
                this.snackBar.open("Could Not connect to Server", "Dismiss");
            }
        );
    }

    postStock(stock: Stock, table: MatTable<any>): void {
        const headers = new HttpHeaders()
            .append(
                'Content-Type',
                'application/json'
            );

        const body = JSON.stringify(stock);

        this.http.post(this.stocksUrl, body, { headers: headers, observe: 'response' })
        .subscribe(
            response => {
                if (response.status == 201) {
                    var stock_new = stock;
                    stock_new.id = <number> response.body!!
                    this.dataSource.push(stock_new);
                    table.renderRows();
                }
                else {
                    this.snackBar.open("An Error Occurred", "Dismiss");
                }
            },
            err => {
                this.snackBar.open("Could Not connect to Server", "Dismiss");
            }
        );

    }

    deleteStock(id: number, table: MatTable<any>): void {
        this.http.delete(this.stocksUrl + id, {observe: 'response'})
        .subscribe(
            response => {
                if (response.status == 200) {
                    for (let i = 0; i < this.dataSource.length; i++) {
                        if (this.dataSource[i].id == id) {
                            this.dataSource.splice(i, 1);
                            break;
                        }
                    }
                    table.renderRows();
                }
                else {
                    this.snackBar.open("An Error Occurred", "Dismiss");
                }
            },
            err => {
                this.snackBar.open("Could Not connect to Server", "Dismiss");
            }
        );
    }
}

