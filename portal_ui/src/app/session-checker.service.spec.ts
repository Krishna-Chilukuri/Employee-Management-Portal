import { TestBed } from '@angular/core/testing';

import { SessionCheckerService } from './session-checker.service';

describe('SessionCheckerService', () => {
  let service: SessionCheckerService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionCheckerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
