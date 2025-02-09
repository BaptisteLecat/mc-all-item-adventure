import { Module } from '@nestjs/common';
import {GameModule} from "./module/game/game.module";
import {ConfigModule} from "@nestjs/config";
import {FirebaseProvider} from "./providers/firebase.provider";

@Module({
  imports: [
    ConfigModule.forRoot({
      isGlobal: true,
      envFilePath: '.env'
    }),
    GameModule
  ],
  controllers: [],
  providers: [FirebaseProvider],
})
export class AppModule {}
