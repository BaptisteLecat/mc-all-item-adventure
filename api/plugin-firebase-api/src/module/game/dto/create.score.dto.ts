import {ApiProperty} from "@nestjs/swagger";
import {IsNotEmpty, IsString} from "class-validator";
import {Timestamp} from "@google-cloud/firestore";

export class CreateScoreDto {

  @ApiProperty()
  @IsNotEmpty()
  @IsString()
  materialName: string;

  @ApiProperty()
  @IsNotEmpty()
  @IsString()
  uidPlayer: string;

  @ApiProperty()
  createdAt: Timestamp;

}