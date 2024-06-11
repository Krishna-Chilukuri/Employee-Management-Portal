import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PromoteToAdminComponent } from './promote-to-admin.component';

describe('PromoteToAdminComponent', () => {
  let component: PromoteToAdminComponent;
  let fixture: ComponentFixture<PromoteToAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PromoteToAdminComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PromoteToAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
