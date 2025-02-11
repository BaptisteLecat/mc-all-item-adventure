import { Game } from "../entities/game.entity";
import { Injectable } from "@nestjs/common";
import { FirestoreDataConverter } from '@google-cloud/firestore';
import * as firebase from 'firebase-admin';

@Injectable()
export class GameConverter implements FirestoreDataConverter<Game> {
  toFirestore(modelObject: Game): firebase.firestore.DocumentData {
    return modelObject.toFirestoreDocument();
  }
  fromFirestore(snapshot: firebase.firestore.QueryDocumentSnapshot<firebase.firestore.DocumentData>): Game {
    const id = snapshot.id;
    const data = snapshot.data();
    return Game.fromFirestoreDocument(id, data);
  }

  fromFirestoreDocumentSnapshot(documentSnapshot: firebase.firestore.DocumentSnapshot<firebase.firestore.DocumentData>): Game {
    const id = documentSnapshot.id;
    const data = documentSnapshot.data();
    return Game.fromFirestoreDocument(id, data);
  }

  async fromFirestoreDocumentReference(documentReference: firebase.firestore.DocumentReference<firebase.firestore.DocumentData>): Promise<Game> {
    const id = documentReference.id;
    const docRef = await documentReference.get();
    const data = docRef.data();
    return Game.fromFirestoreDocument(id, data);
  }

}