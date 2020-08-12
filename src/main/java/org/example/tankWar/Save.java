package org.example.tankWar;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Save {

    private boolean gameContinued;

    private Position playerPosition;

    private List<Position> enemyPosition;



    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Position{
        private int x,y;
        private Direction direction;


    }

}
