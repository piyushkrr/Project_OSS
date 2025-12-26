import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ProfileComponent } from './profile.component';
import { AuthService } from '../auth.service';
import { of, throwError } from 'rxjs';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormsModule } from '@angular/forms';

describe('ProfileComponent', () => {
  let component: ProfileComponent;
  let fixture: ComponentFixture<ProfileComponent>;
  let authService: jasmine.SpyObj<AuthService>;

  beforeEach(async () => {
    const spy = jasmine.createSpyObj('AuthService', ['getProfile', 'updateProfile']);
    
    await TestBed.configureTestingModule({
      declarations: [ ProfileComponent ],
      imports: [ HttpClientTestingModule, FormsModule ],
      providers: [
        { provide: AuthService, useValue: spy }
      ]
    }).compileComponents();

    authService = TestBed.get(AuthService);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ProfileComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    authService.getProfile.and.returnValue(of({}));
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('should load profile on init', () => {
    const mockUser = { firstName: 'John', lastName: 'Doe', email: 'john@example.com' };
    authService.getProfile.and.returnValue(of(mockUser));
    fixture.detectChanges();
    expect(component.user.firstName).toBe('John');
  });

  it('should toggle edit mode', () => {
    component.toggleEdit();
    expect(component.editMode).toBeTrue();
    component.toggleEdit();
    expect(component.editMode).toBeFalse();
  });

  it('should save profile', () => {
    const updatedUser = { firstName: 'Jane', lastName: 'Doe' };
    authService.updateProfile.and.returnValue(of(updatedUser));
    
    component.user = { firstName: 'Jane' };
    component.saveProfile();
    
    expect(authService.updateProfile).toHaveBeenCalled();
    expect(component.message).toBe('Profile updated successfully!');
    expect(component.editMode).toBeFalse();
  });
});
