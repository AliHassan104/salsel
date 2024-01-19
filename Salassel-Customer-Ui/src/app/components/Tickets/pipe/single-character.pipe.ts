import { Pipe, PipeTransform } from "@angular/core";

@Pipe({
  name: "singleCharacter",
})
export class SingleCharacterPipe implements PipeTransform {
  transform(value: string): string {
    if (!value) {
      return "";
    }
    return value.charAt(0).toUpperCase();
  }
}
