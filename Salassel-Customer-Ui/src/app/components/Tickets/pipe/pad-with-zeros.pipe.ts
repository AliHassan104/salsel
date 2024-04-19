import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: "padWithZeros",
})
export class PadWithZerosPipe implements PipeTransform {
  transform(value: number, length: number): string {
    let strValue = value.toString();
    while (strValue.length < length) {
      strValue = "0" + strValue;
    }
    return strValue;
  }
}
