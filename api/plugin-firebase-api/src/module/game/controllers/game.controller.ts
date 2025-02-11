import {Body, Controller, Get, NotFoundException, Param, Post} from '@nestjs/common';
import {GamesService} from "../services/game.service";
import {Game} from "../entities/game.entity";
import {CreateGameDto} from "../dto/create.game.dto";
import {ChestCoord} from "../entities/chestCoord.entity";
import {Score} from "../entities/score.entity";
import {CreateScoreDto} from "../dto/create.score.dto";

@Controller({
  version: '1',
  path: 'games',
})
export class GameController {
  constructor(private readonly appService: GamesService) {}

  @Get(":id")
  async getGame(@Param('id') id: string): Promise<Game> {
    const game = await this.appService.findOne(id);

    if (!game) throw new NotFoundException('Game not found');

    return game;
  }

  @Post()
  async newGame(@Body() createGameDto : CreateGameDto): Promise<Game> {

    const chestCoord = createGameDto.chestCoord;
    const scores = createGameDto.scores.map(score => new Score(score.materialName, score.uidPlayer, score.createdAt));
    const gameToCreate = new Game(null, createGameDto.gameKey, new ChestCoord(chestCoord.x, chestCoord.y, chestCoord.z), scores);
    return this.appService.create(gameToCreate);
  }

  @Post(":id")
  async addScore(@Param('id') id: string, @Body() createScoreDto : CreateScoreDto): Promise<Score> {
    return this.appService.addScore(id, new Score(createScoreDto.materialName, createScoreDto.uidPlayer));
  }
  
}
