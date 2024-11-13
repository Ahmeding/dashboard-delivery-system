import { TestBed } from '@angular/core/testing';

import { InputFillUtilsService } from './input-fill-utils.service';

describe('InputFillUtilsService', () => {
  let service: InputFillUtilsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InputFillUtilsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
