import { TestBed } from '@angular/core/testing';

import { IsLogedinGuard } from './is-logedin.guard';

describe('IsLogedinGuard', () => {
  let guard: IsLogedinGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    guard = TestBed.inject(IsLogedinGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
