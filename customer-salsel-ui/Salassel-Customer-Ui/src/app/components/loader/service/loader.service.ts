import { Injectable } from "@angular/core";
import { BehaviorSubject, Observable } from "rxjs";

@Injectable({
  providedIn: "root",
})
export class LoaderService {
  private isLoadingSubject: BehaviorSubject<boolean> =
    new BehaviorSubject<boolean>(false);

  constructor() {}

  // Show loader
  showLoader(): void {
    this.isLoadingSubject.next(true);
  }

  // Hide loader
  hideLoader(): void {
    this.isLoadingSubject.next(false);
  }

  // Observable to track loader status
  get isLoading$(): Observable<boolean> {
    return this.isLoadingSubject.asObservable();
  }
}
