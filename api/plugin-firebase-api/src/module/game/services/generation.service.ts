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

  async create(id: string, gameKey: string, chestCoord: ChestCoord, scores: Score[]): Promise<Game> {
    const game = new Game(id, gameKey, chestCoord, scores);
    await this.firestoreProvider.getFirestore().collection(GamesService.collection).doc(id).withConverter(this.gameConverter).set(game.toFirestoreDocument());

    return game;
  }

  async update(game: Game): Promise<Game> {

    await this.firestoreProvider.getFirestore().collection(GamesService.collection).doc(game.id).withConverter(this.gameConverter).set(game.toFirestoreDocument());

    return game;
  }

}