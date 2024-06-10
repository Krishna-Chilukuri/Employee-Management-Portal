import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PromoteEmployeeComponent } from './promote-employee.component';

describe('PromoteEmployeeComponent', () => {
  let component: PromoteEmployeeComponent;
  let fixture: ComponentFixture<PromoteEmployeeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PromoteEmployeeComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PromoteEmployeeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
