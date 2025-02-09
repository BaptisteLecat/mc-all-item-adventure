import { Inject, Injectable } from '@nestjs/common';
import { FirebaseProvider } from '../../../providers/firebase.provider';
import { Game } from '../entities/game.entity';
import {GameConverter} from "../converters/game.converter";

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
}