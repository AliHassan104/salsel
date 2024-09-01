import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WebRateViewComponent } from './web-rate-view.component';

describe('WebRateViewComponent', () => {
  let component: WebRateViewComponent;
  let fixture: ComponentFixture<WebRateViewComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [WebRateViewComponent]
    });
    fixture = TestBed.createComponent(WebRateViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
