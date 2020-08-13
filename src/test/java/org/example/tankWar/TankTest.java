package org.example.tankWar;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
class TankTest {

    @org.junit.jupiter.api.Test
    void getImage() {
        for (Direction direction : Direction.values()){
            Tank tank = new Tank(0, 0, false, direction);
            assertTrue(tank.getImage().getWidth(null) > 0, direction + " has problem.");

            Tank enemyTank = new Tank(0, 0, true, direction);
            assertTrue(enemyTank.getImage().getWidth(null) > 0, direction + " has problem.");
        }
    }
    @org.junit.jupiter.api.Test
    void save() throws IOException {
        String dest = "tmp/game.sav";
        GameClient.getInstance().save(dest);

        byte[] bytes = Files.readAllBytes(Paths.get(dest));
        Save save = JSON.parseObject(bytes, Save.class);
        assertTrue(save.isGameContinued());

        assertEquals(12, save.getEnemyPosition().size());
    }
}