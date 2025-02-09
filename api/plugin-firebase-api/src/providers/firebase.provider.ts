import * as admin from 'firebase-admin';
import { Injectable } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';

@Injectable()
export class FirebaseProvider {
  private initialized = false;

  constructor(private readonly configService: ConfigService) { }

  private initialize() {

    if (process.env.NODE_ENV == 'production') {
      admin.initializeApp();
    } else {
      // Set the Firebase Auth emulator host : https://stackoverflow.com/a/77929815/19101705
      process.env['FIREBASE_AUTH_EMULATOR_HOST'] = '127.0.0.1:9099';
      process.env['FIRESTORE_EMULATOR_HOST'] = '127.0.0.1:8080';

      // Initialize Firebase Admin SDK
      admin.initializeApp(
        {
          projectId: "weatherapp-journey",
        }
      );
    }

    this.initialized = true;
  }

  getFirestore(): FirebaseFirestore.Firestore {
    if (!this.initialized) {
      if (!admin.apps.length) { // Vérifiez que l'application n'est pas déjà initialisée
        this.initialize();
      }
    }

    return admin.firestore();
  }

  getAuth(): admin.auth.Auth {
    if (!this.initialized) {
      if (!admin.apps.length) { // Vérifiez que l'application n'est pas déjà initialisée
        this.initialize();
      }
    }
    return admin.auth();
  }
}