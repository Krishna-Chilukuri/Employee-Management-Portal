import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DemoteOwnerComponent } from './demote-owner.component';

describe('DemoteOwnerComponent', () => {
  let component: DemoteOwnerComponent;
  let fixture: ComponentFixture<DemoteOwnerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DemoteOwnerComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DemoteOwnerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
