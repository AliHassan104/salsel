import { Component, ElementRef, ViewChild } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { ResetPasswordService } from '../new-password/service/reset-password.service';

@Component({
  selector: "app-verification",
  templateUrl: "./verification.component.html",
  styleUrls: ["./verification.component.scss"],
  providers: [MessageService],
})
export class VerificationComponent {
  otpExpired: boolean = true;
  expireTime = 120;
  timerInterval: any;
  userMail;
  verifyForm!: FormGroup;

  @ViewChild("input1") input1: ElementRef;
  @ViewChild("input2") input2: ElementRef;
  @ViewChild("input3") input3: ElementRef;
  @ViewChild("input4") input4: ElementRef;
  @ViewChild("input5") input5: ElementRef;
  @ViewChild("input6") input6: ElementRef;

  constructor(
    private fb: FormBuilder,
    private messageService: MessageService,
    private route: ActivatedRoute,
    private router: Router,
    private resetPassService: ResetPasswordService
  ) {}

  ngOnInit() {
    this.verifyForm = this.fb.group({
      input1: ["", Validators.required],
      input2: ["", Validators.required],
      input3: ["", Validators.required],
      input4: ["", Validators.required],
      input5: ["", Validators.required],
      input6: ["", Validators.required],
    });

    this.subscribeToInputChanges();
    this.otpTimer();
    this.queryParamSetup();
  }

  queryParamSetup() {
    this.route.queryParams.subscribe((params: any) => {
      console.log(params);
      this.userMail = params?.email;
    });
  }

  otpTimer() {
    this.timerInterval = setInterval(() => {
      if (this.expireTime > 0) {
        this.expireTime--;
      } else {
        this.otpExpired = false;
        clearInterval(this.timerInterval); // Clear the interval when time reaches zero
      }
    }, 1000); // Update every second (1000 milliseconds)
  }

  resendOtp() {
    if (this.userMail != null) {
      this.resetPassService.forgotPassword({ email: this.userMail }).subscribe(
        (res: any) => {
          this.messageService.add({
            severity: "success",
            summary: "Success",
            detail: res,
          });
          this.otpExpired = true;
          this.expireTime = 120;
          this.otpTimer();
        },
        (error: any) => {
          this.messageService.add({
            severity: "error",
            summary: "Error",
            detail: error?.error?.error,
          });
        }
      );
    }
  }

  subscribeToInputChanges() {
    this.verifyForm
      .get("input1")
      .valueChanges.subscribe((value) =>
        this.onInput(value, this.input2, null, this.input1)
      );
    this.verifyForm
      .get("input2")
      .valueChanges.subscribe((value) =>
        this.onInput(value, this.input3, this.input1, this.input2)
      );
    this.verifyForm
      .get("input3")
      .valueChanges.subscribe((value) =>
        this.onInput(value, this.input4, this.input2, this.input3)
      );
    this.verifyForm
      .get("input4")
      .valueChanges.subscribe((value) =>
        this.onInput(value, this.input5, this.input3, this.input4)
      );
    this.verifyForm
      .get("input5")
      .valueChanges.subscribe((value) =>
        this.onInput(value, this.input6, this.input4, this.input5)
      );

    this.verifyForm
      .get("input6")
      .valueChanges.subscribe((value) =>
        this.onInput(value, null, this.input5, this.input6)
      );
  }

  onInput(
    value: string,
    nextInput: ElementRef | null,
    previousInput: ElementRef | null,
    currentInput: ElementRef
  ) {
    if (value !== "") {
      setTimeout(() => {
        if (nextInput && nextInput.nativeElement) {
          nextInput.nativeElement.focus();
        }
      });
    } else if (previousInput && previousInput.nativeElement) {
      setTimeout(() => {
        previousInput.nativeElement.focus();
      });
    }
  }

  onCancel() {
    this.router.navigate(["login"]);
  }

  onVerify(data) {
    const otp =
      data.input1 +
      data.input2 +
      data.input3 +
      data.input4 +
      data.input5 +
      data.input6;
    console.log(data, otp);

    if (this.verifyForm.valid) {
      const params = { code: otp };
      this.resetPassService.isValidOtp(params).subscribe(
        (res: any) => {
          const queryParams = { email: this.userMail, code: otp };
          this.router.navigate(["reset-password"], {
            queryParams: queryParams,
          });
        },
        (error: any) => {

          this.showError();
        }
      );
    } else {
      this.messageService.add({
        severity: "error",
        summary: "Error",
        detail: "Please fill all the required fields",
      });
    }
  }

  showError() {
    this.messageService.add({
      severity: "error",
      summary: "Error",
      detail: "Invalid OTP",
    });
  }
}
