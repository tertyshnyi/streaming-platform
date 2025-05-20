import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { UserService } from '../../../core/services/user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-registration',
  imports: [CommonModule, ReactiveFormsModule],
  standalone: true,
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent implements OnInit {
  registerForm!: FormGroup;

  constructor(private fb: FormBuilder, private userService: UserService, private router: Router) {}

  ngOnInit() {
    this.registerForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      phoneNumber: ['', [Validators.required, Validators.pattern(/^\+?\d{7,15}$/)]],
      profileImg: [''],
    });
  }

  get name() { return this.registerForm.get('name')!; }
  get email() { return this.registerForm.get('email')!; }
  get password() { return this.registerForm.get('password')!; }
  get phoneNumber() { return this.registerForm.get('phoneNumber')!; }
  get profileImg() { return this.registerForm.get('profileImg')!; }

  onSubmit() {
    if (this.registerForm.valid) {
      const formValue = this.registerForm.value;

      this.userService.register(formValue).subscribe({
        next: (response) => {
          console.log('User registered:', response);
          this.router.navigate(['/']);
        },
        error: (error) => {
          console.error('Registration error:', error);
        }
      });

    } else {
      this.registerForm.markAllAsTouched();
    }
  }
}
