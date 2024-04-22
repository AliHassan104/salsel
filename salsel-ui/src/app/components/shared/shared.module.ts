import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DaysAgoPipe } from '../Tickets/pipe/days-ago.pipe';
import { ExtractFileNamePipe } from '../Tickets/pipe/extract-file-name.pipe';
import { PadWithZerosPipe } from '../Tickets/pipe/pad-with-zeros.pipe';
import { PartialEmailPipe } from '../Tickets/pipe/partial-email.pipe';
import { SingleCharacterPipe } from '../Tickets/pipe/single-character.pipe';



@NgModule({
  declarations: [
    PadWithZerosPipe,
    DaysAgoPipe,
    PartialEmailPipe,
    SingleCharacterPipe,
    ExtractFileNamePipe,
  ],
  exports:[
    PadWithZerosPipe,
    DaysAgoPipe,
    PartialEmailPipe,
    SingleCharacterPipe,
    ExtractFileNamePipe,
  ],
  imports: [CommonModule],
})
export class SHAREDModule {}
