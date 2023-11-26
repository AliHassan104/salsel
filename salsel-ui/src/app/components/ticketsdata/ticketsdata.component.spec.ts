import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TicketsdataComponent } from './ticketsdata.component';

describe('TicketsdataComponent', () => {
  let component: TicketsdataComponent;
  let fixture: ComponentFixture<TicketsdataComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TicketsdataComponent]
    });
    fixture = TestBed.createComponent(TicketsdataComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
