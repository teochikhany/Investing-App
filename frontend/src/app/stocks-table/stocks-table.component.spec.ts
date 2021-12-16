import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StockTableComponent } from './stocks-table.component';

describe('HomePageComponent', () => {
  let component: StockTableComponent;
  let fixture: ComponentFixture<StockTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StockTableComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StockTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
