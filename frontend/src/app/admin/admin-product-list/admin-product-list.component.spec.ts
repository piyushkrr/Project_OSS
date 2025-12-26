import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { AdminProductListComponent } from './admin-product-list.component';
import { ProductService } from '../../product.service';
import { of } from 'rxjs';

describe('AdminProductListComponent', () => {
  let component: AdminProductListComponent;
  let fixture: ComponentFixture<AdminProductListComponent>;
  let productService: ProductService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminProductListComponent ],
      imports: [ HttpClientTestingModule, RouterTestingModule ],
      providers: [ ProductService ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminProductListComponent);
    component = fixture.componentInstance;
    productService = TestBed.inject(ProductService);

    // Mock getProducts
    spyOn(productService, 'getProducts').and.returnValue(of([]));

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
