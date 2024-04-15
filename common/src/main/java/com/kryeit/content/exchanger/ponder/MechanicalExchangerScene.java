package com.kryeit.content.exchanger.ponder;

import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import com.simibubi.create.foundation.ponder.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;

public class MechanicalExchangerScene {

    public static void showPonder(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("mechanical_exchanger", "Getting new kind of currencies with the Mechanical Exchanger");
        scene.configureBasePlate(0, 0, 5);
        scene.world.showSection(util.select.layer(0), Direction.UP);

        Selection body = util.select.cuboid(new BlockPos(0, 1, 0), new Vec3i(5, 1, 5));
        Selection belt = util.select.cuboid(new BlockPos(3, 2, 2), new BlockPos(3, 2, 4));

        Selection exchanger = util.select.position(2, 1, 2);
        scene.world.showSection(body, Direction.UP);
        scene.world.showSection(belt, Direction.UP);
        scene.idle(20);
        scene.world.setKineticSpeed(body, 100);
        scene.world.setKineticSpeed(belt, 100);

        scene.overlay.showText(150)
                .placeNearTarget()
                .text("A exchanger can have 2 different modes: to a smaller currency or to a bigger currency")
                .pointAt(util.vector.topOf(2, 1, 2));
        scene.idle(150);

        scene.world.setKineticSpeed(body, -100);
        scene.world.setKineticSpeed(belt, -100);
        scene.world.toggleRedstonePower(body);

        scene.idle(30);
        scene.overlay.showText(150)
                .placeNearTarget()
                .text("Changing the rotation direction will change the mode of the exchanger")
                .pointAt(util.vector.topOf(2, 1, 2));
        scene.idle(30);

        scene.markAsFinished();
    }
}
