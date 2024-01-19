import { Pipe, PipeTransform } from "@angular/core";

@Pipe({
  name: "daysAgo",
})
export class DaysAgoPipe implements PipeTransform {
  transform(value: string | Date): string {
    const createdDate = new Date(value);
    const currentDate = new Date();

    const timeDifference = currentDate.getTime() - createdDate.getTime();
    const daysDifference = Math.floor(timeDifference / (1000 * 60 * 60 * 24));

    if (daysDifference === 0) {
      return "Today";
    } else if (daysDifference === 1) {
      return "Yesterday";
    } else {
      return daysDifference + " days ago";
    }
  }
}
