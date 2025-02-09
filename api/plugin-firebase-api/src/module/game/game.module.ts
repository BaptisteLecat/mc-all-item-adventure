import { Module } from '@nestjs/common';
import {GameController} from "./controllers/game.controller";
import {GamesService} from "./services/generation.service";
import {FirebaseProvider} from "../../providers/firebase.provider";
import {GameConverter} from "./converters/game.converter";

@Module({
  controllers: [GameController],
  providers: [GamesService, GameConverter, FirebaseProvider]
})
export class GameModule {}
