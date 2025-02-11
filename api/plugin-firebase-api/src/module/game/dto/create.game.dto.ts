import {IsArray, IsNotEmpty, IsObject, IsString} from "class-validator";
import {ApiProperty} from "@nestjs/swagger";
import {CreateChestCoordDto} from "./create.chestCoord.dto";
import {CreateScoreDto} from "./create.score.dto";

export class CreateGameDto {

  @ApiProperty()
  @IsString()
  @IsNotEmpty()
  gameKey: string;

  @ApiProperty()
  @IsObject()
  @IsNotEmpty()
  chestCoord: CreateChestCoordDto

  @ApiProperty()
  @IsArray()
  @IsNotEmpty()
  scores: CreateScoreDto[]

}