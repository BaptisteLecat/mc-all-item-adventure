import {ApiProperty} from "@nestjs/swagger";
import {IsNotEmpty, IsNumber} from "class-validator";

export class CreateChestCoordDto {

  @ApiProperty()
  @IsNotEmpty()
  @IsNumber()
  x: number;

  @ApiProperty()
  @IsNotEmpty()
  @IsNumber()
  y: number;

  @ApiProperty()
  @IsNotEmpty()
  @IsNumber()
  z: number;

}