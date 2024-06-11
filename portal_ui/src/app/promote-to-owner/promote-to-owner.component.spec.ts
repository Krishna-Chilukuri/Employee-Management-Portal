import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PromoteToOwnerComponent } from './promote-to-owner.component';

describe('PromoteToOwnerComponent', () => {
  let component: PromoteToOwnerComponent;
  let fixture: ComponentFixture<PromoteToOwnerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PromoteToOwnerComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PromoteToOwnerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
