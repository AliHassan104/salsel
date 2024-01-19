import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: "extractNumeric",
})
export class ExtractNumericPipe implements PipeTransform {
  transform(inputString: string): string {
    const index = inputString?.indexOf(",");

    return index !== -1 ? inputString?.substring(0, index) : inputString;
  }
}
