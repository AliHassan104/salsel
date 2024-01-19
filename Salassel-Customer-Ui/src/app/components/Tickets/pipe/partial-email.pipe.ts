import { Pipe, PipeTransform } from "@angular/core";

@Pipe({
  name: "partialEmail",
})
export class PartialEmailPipe implements PipeTransform {
  transform(email: string): string {
    const [username, domain] = email.split("@");
    const obscuredUsername = username.substring(0, 3) + "***";
    return `${obscuredUsername}@${domain}`;
  }
}
