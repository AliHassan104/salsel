import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WebRateComponent } from './web-rate.component';

describe('WebRateComponent', () => {
  let component: WebRateComponent;
  let fixture: ComponentFixture<WebRateComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [WebRateComponent]
    });
    fixture = TestBed.createComponent(WebRateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
