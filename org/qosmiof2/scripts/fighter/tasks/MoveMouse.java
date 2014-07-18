package org.qosmiof2.scripts.fighter.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt6.ClientContext;
import org.qosmiof2.scripts.framework.Node;

public class MoveMouse extends Node {

	public MoveMouse(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		return ctx.players.local().interacting().valid();
	}

	@Override
	public void execute() {
		switch (Random.nextInt(1, 10)) {

		case 6:
			ctx.mouse.move(Random.nextInt(0, 800), Random.nextInt(0, 600));
			Condition.sleep(Random.nextInt(500, 1000));
			break;

		default:
			Condition.sleep(Random.nextInt(500, 1000));
			break;
		}

	}

}
