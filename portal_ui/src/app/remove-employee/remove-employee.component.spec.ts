import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RemoveEmployeeComponent } from './remove-employee.component';

describe('RemoveEmployeeComponent', () => {
  let component: RemoveEmployeeComponent;
  let fixture: ComponentFixture<RemoveEmployeeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RemoveEmployeeComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(RemoveEmployeeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
