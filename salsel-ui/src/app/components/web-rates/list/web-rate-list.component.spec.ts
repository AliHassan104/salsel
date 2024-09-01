import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WebRateListComponent } from './web-rate-list.component';

describe('WebRateListComponent', () => {
  let component: WebRateListComponent;
  let fixture: ComponentFixture<WebRateListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [WebRateListComponent]
    });
    fixture = TestBed.createComponent(WebRateListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
