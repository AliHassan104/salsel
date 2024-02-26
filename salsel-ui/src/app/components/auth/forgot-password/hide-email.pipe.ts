import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: "hideEmail",
})
export class HideEmailPipe implements PipeTransform {
  transform(email: string): string {
    if (!email) {
      return "";
    }

    const [username, domain] = email.split("@");
    const hiddenUsername =
      username.slice(0, 2) + "*".repeat(username.length - 2);

    return `${hiddenUsername}@${domain}`;
  }
}
