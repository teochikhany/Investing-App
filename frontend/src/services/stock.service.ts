import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Stock } from '../models/stock';


@Injectable({
    providedIn: 'root'
})
export class StockService {

    private stocksUrl = 'http://localhost:8080/api/v1/stocks/';  // URL to web api
    private dataSource: Stock[] = [];

    constructor(private http: HttpClient) { }

    getSource(): Stock[] {
        return this.dataSource
    }

    setSource(stocks: Stock[]): void {
        this.dataSource = stocks;
    }

    getStocks(): Observable<Stock[]> {
        return this.http.get<Stock[]>(this.stocksUrl)
    }

    postStock(stock: Stock): void {

        this.dataSource.push(stock);

        const headers = new HttpHeaders()
            .append(
                'Content-Type',
                'application/json'
            );

        const body = JSON.stringify(stock);

        this.http.post(this.stocksUrl, body, {
            headers: headers
        }).subscribe();

    }

    deleteStock(id: number): void {

        for (let i = 0; i < this.dataSource.length; i++) {
            if (this.dataSource[i].id == id) {
                this.dataSource.splice(i, 1);
                break;
            }
        }

        this.http.delete(this.stocksUrl + id).subscribe();
    }
}

