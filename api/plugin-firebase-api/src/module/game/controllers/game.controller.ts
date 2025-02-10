import {Controller, Get, NotFoundException, Param, Post} from '@nestjs/common';
import {GamesService} from "../services/generation.service";
import {Game} from "../entities/game.entity";

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
  async newGame() {

  }

}
