import { Inject, Injectable } from '@nestjs/common';
import { FirebaseProvider } from '../../../providers/firebase.provider';
import { Game } from '../entities/game.entity';
import {GameConverter} from "../converters/game.converter";
import {ChestCoord} from "../entities/chestCoord.entity";
import {Score} from "../entities/score.entity";

@Injectable()
export class GamesService {
  static readonly collection : string = 'games';
  constructor(@Inject(FirebaseProvider) private readonly firestoreProvider: FirebaseProvider, private gameConverter: GameConverter) {}

  async findOne(id: string) : Promise<Game | undefined> {
    const game = await this.firestoreProvider.getFirestore().collection(GamesService.collection).doc(id).withConverter(this.gameConverter).get();
    if (!game.exists) {
      return undefined;
    }
    return this.gameConverter.fromFirestoreDocumentSnapshot(game);
  }

  async create(game: Game): Promise<Game> {
    const id = this.firestoreProvider.getFirestore().collection(GamesService.collection).doc().id;
    game.id = id;
    await this.firestoreProvider.getFirestore().collection(GamesService.collection).doc(id).withConverter(this.gameConverter).set(game);

    return game;
  }

  async addScore(gameId: string, score: Score): Promise<Score> {
    const game = await this.findOne(gameId);
    if(!game){
      throw Error("La game indiquée n'existe pas")
    }
    game.scores.push(score);

    await this.firestoreProvider.getFirestore().collection(GamesService.collection).doc(gameId).withConverter(this.gameConverter).set(game);
    return score;
  }

}