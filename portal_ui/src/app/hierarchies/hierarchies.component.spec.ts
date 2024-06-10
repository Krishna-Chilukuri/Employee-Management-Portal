import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HierarchiesComponent } from './hierarchies.component';

describe('HierarchiesComponent', () => {
  let component: HierarchiesComponent;
  let fixture: ComponentFixture<HierarchiesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HierarchiesComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(HierarchiesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
