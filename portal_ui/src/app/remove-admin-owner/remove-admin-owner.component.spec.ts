import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RemoveAdminOwnerComponent } from './remove-admin-owner.component';

describe('RemoveAdminOwnerComponent', () => {
  let component: RemoveAdminOwnerComponent;
  let fixture: ComponentFixture<RemoveAdminOwnerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RemoveAdminOwnerComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(RemoveAdminOwnerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
