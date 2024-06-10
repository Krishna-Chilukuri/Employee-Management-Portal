import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DemoteEmployeeComponent } from './demote-employee.component';

describe('DemoteEmployeeComponent', () => {
  let component: DemoteEmployeeComponent;
  let fixture: ComponentFixture<DemoteEmployeeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DemoteEmployeeComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DemoteEmployeeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
