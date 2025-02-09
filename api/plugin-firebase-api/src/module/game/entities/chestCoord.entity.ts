import { Timestamp } from "@google-cloud/firestore";

export class ChestCoord {
  x: number;
  y: number;
  z: number;

  public constructor(x: number, y: number, z: number) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  static fromJson(data: any): ChestCoord {
    return new ChestCoord(data.x, data.y, data.z);
  }

  toFirestoreDocument(): any {
    return {
      x: this.x,
      y: this.y,
      z: this.z
    };
  }

  toJson(): any {
    return {
      x: this.x,
      y: this.y,
      z: this.z
    };
  }
}