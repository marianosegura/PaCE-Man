//
// Created by valva on 8/4/2020.
//

#include "Game.h"

Game create_game(){
    Game game;
    game.level = 1;
    game.lives = 3;
    game.score = 0;
    game.pac_man = create_pac_man();
    game.pill_state = false;
    game.pills = NULL;
    game.fruits = NULL;
    game.ghosts[0] = create_ghost(1);
    game.ghosts[1] = create_ghost(2);
    game.ghosts[2] = create_ghost(3);
    game.ghosts[3] = create_ghost(4);
    return game;
}

void update_game_level(Game *game,unsigned int new_level){
    game->level = new_level;
}

unsigned int get_game_level(Game *game){
    return game->level;
}

void update_game_lives(Game *game,unsigned int new_lives){
    game->lives = new_lives;
}

unsigned int get_game_lives(Game *game){
    return game->lives;
}

void update_game_score(Game *game,unsigned int added_score){
    game->score += added_score;
}

unsigned int get_game_score(Game *game){
    return game->score;
}

void update_game_pill_active(Game *game,bool new_pill_state){
    game->pill_state = new_pill_state;
}

bool get_pill_state(Game *game){
    return game->pill_state;
}

void update_game_pac_man(Game *game,Pair new_pos,int new_sprite){
    modify_pac_man(&game->pac_man,new_pos,new_sprite);
}

void update_game_pac_man_pos(Game *game,Pair new_pos){
    modify_pac_man_position(&game->pac_man, new_pos);
}

void update_game_ghosts_pos(Game *game,Pair* new_pos){
    modify_ghost_position(&game->ghosts[0],new_pos[0]);
    modify_ghost_position(&game->ghosts[1],new_pos[1]);
    modify_ghost_position(&game->ghosts[2],new_pos[2]);
    modify_ghost_position(&game->ghosts[3],new_pos[3]);
}

void add_pill_to_game(Game *game, Pill new_pill){
    game->pills = add_p_node(game->pills,new_pill);
}

void delete_pill_from_game(Game *game,Pill pill){
    game->pills = delete_p_node(game->pills,pill);
}