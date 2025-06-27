import { TestBed } from '@angular/core/testing';

import { MenuMissionCardService } from './menu-mission-card.service';

describe('MenuMissionCardService', () => {
  let service: MenuMissionCardService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MenuMissionCardService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
