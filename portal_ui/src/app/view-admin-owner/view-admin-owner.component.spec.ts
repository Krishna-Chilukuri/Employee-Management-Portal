import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewAdminOwnerComponent } from './view-admin-owner.component';

describe('ViewAdminOwnerComponent', () => {
  let component: ViewAdminOwnerComponent;
  let fixture: ComponentFixture<ViewAdminOwnerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ViewAdminOwnerComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ViewAdminOwnerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
