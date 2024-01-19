import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: "extractFileName",
})
export class ExtractFileNamePipe implements PipeTransform {
  transform(url: string): string {
    if (!url) {
      return "";
    }

    const parts = url.split("/");
    return parts[parts.length - 1];
  }
}
