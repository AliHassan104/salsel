import { TestBed } from '@angular/core/testing';

import { WebRatesService } from './web-rates.service';

describe('WebRatesService', () => {
  let service: WebRatesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WebRatesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
