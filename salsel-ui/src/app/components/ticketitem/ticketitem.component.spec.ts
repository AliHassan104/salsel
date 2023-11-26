import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TicketitemComponent } from './ticketitem.component';

describe('TicketitemComponent', () => {
  let component: TicketitemComponent;
  let fixture: ComponentFixture<TicketitemComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TicketitemComponent]
    });
    fixture = TestBed.createComponent(TicketitemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
