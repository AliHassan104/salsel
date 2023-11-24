import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TicketformComponent } from './ticketform.component';

describe('TicketformComponent', () => {
  let component: TicketformComponent;
  let fixture: ComponentFixture<TicketformComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TicketformComponent]
    });
    fixture = TestBed.createComponent(TicketformComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
