import { Timestamp } from "@google-cloud/firestore";
import {Score} from "./score.entity";
import {ChestCoord} from "./chestCoord.entity";

export class Game {
  id: string;
  gameKey: string;
  chestCoord: ChestCoord;
  scores: Score[];

  public constructor(id: string, gameKey: string, chestCoord: ChestCoord, scores: Score[]) {
    this.id = id;
    this.gameKey = gameKey;
    this.chestCoord = chestCoord;
    this.scores = scores;
  }

  static fromFirestoreDocument(id: any, data: any): Game {
    return new Game(id, data.gameKey, data.chestCoord, data.scores);
  }

  static fromJson(data: any): Game {
    return new Game(data.id, data.gameKey, data.chestCoord, data.scores,);
  }

  toFirestoreDocument(): any {
    return {
      id: this.id,
      gameKey: this.gameKey,
      chestCoord: this.chestCoord.toFirestoreDocument(),
      scores: this.scores.map((score) => score.toFirestoreDocument())
    };
  }

  toJson(): any {
    return {
      id: this.id,
      gameKey: this.gameKey,
      chestCoord: this.chestCoord.toJson(),
      scores: this.scores.map((score) => score.toJson())
    };
  }
}