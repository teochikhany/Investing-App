import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Stock } from '../models/stock';
import { MatTable } from '@angular/material/table';
import { NotificationService } from './notification.service';


@Injectable({
    providedIn: 'root'
})
export class StockService {

    private stocksUrl = 'http://localhost:8080/api/v1/stocks/';  // URL to web api
    // private stocksUrl = 'http://backend:8080/api/v1/stocks/';
    private dataSource: Stock[] = [];

    constructor(private http: HttpClient, private notification: NotificationService) { }

    getSource(): Stock[] {
        return this.dataSource
    }

    private setSource(stocks: Stock[]): void {
        this.dataSource = stocks;
    }

    getStocks(): void {

        var request = this.http.get<Stock[]>(this.stocksUrl, {observe: 'response'})
        request.subscribe({
            next: (value: HttpResponse<Stock[]>) => this.setSource(value.body!!) ,
            error: err => { this.notification.showError(err); }
        });
    }

    postStock(stock: Stock, table: MatTable<any>): void {
        const headers = new HttpHeaders()
            .append(
                'Content-Type',
                'application/json'
            );

        var request = this.http.post(this.stocksUrl, stock, {headers: headers, observe: 'response'})
        request.subscribe({
            next: (value: HttpResponse<Object>) => {
                    var stock_new = stock;
                    stock_new.id = <number> value.body!!
                    this.dataSource.push(stock_new);
                    table.renderRows();
                },
            error: err => { this.notification.showError(err); }
        });
    }

    deleteStock(id: number, table: MatTable<any>): void {

        var request = this.http.delete(this.stocksUrl + id, {observe: 'response'})
        request.subscribe({
            next: (_: HttpResponse<Object>) => {
                for (let i = 0; i < this.dataSource.length; i++) {
                        if (this.dataSource[i].id == id) {
                            this.dataSource.splice(i, 1);
                            break;
                        }
                    }
                table.renderRows();
                },
            error: err => { this.notification.showError(err); }
        });
    }
}

