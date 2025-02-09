import { Timestamp } from "@google-cloud/firestore";

export class Score {
  materialName: string;
  uidPlayer: string;
  createdAt: Timestamp;

  public constructor(materialName: string, uidPlayer: string, createdAt: Timestamp = Timestamp.now()) {
    this.materialName = materialName;
    this.uidPlayer = uidPlayer;
    this.createdAt = createdAt;
  }

  static fromJson(data: any): Score {
    return new Score(data.materialName, data.uidPlayer, data.createdAt);
  }

  toFirestoreDocument(): any {
    return {
      materialName: this.materialName,
      uidPlayer: this.uidPlayer,
      createdAt: this.createdAt
    };
  }

  toJson(): any {
    return {
      materialName: this.materialName,
      uidPlayer: this.uidPlayer,
      createdAt: this.createdAt
    };
  }
}