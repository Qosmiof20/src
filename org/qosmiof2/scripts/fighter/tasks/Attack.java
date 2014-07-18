package org.qosmiof2.scripts.fighter.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.Random;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Npc;
import org.qosmiof2.scripts.fighter.gui.Gui;
import org.qosmiof2.scripts.framework.Node;

public class Attack extends Node {

	private Gui gui;

	public Attack(ClientContext ctx, Gui gui) {
		super(ctx);
		this.gui = gui;
	}

	private Filter<Npc> filter = new Filter<Npc>() {

		public boolean accept(Npc npc) {
			if (npc.healthPercent() == 100) {
				return true;
			}
			return false;
		}

	};

	@Override
	public boolean activate() {

		return !ctx.players.local().interacting().valid()
				&& ctx.players.local().animation() == -1
				&& !ctx.npcs.select().select(filter).name(gui.getNpcs())
						.within(10).isEmpty();
	}

	@Override
	public void execute() {
		Npc npc = ctx.npcs.nearest().first().poll();

		if (npc.inViewport()) {

			if (npc.interact("Attack")) {

				while (ctx.players.local().inMotion()) {
					Condition.sleep();
				}

			}
		} else {

			switch (Random.nextInt(1, 10)) {
			case 5:
				ctx.camera.turnTo(npc);
				ctx.camera.pitch(Random.nextInt(20, 84));
				break;

			default:
				ctx.camera.turnTo(npc);
				break;
			}

			if (!npc.inViewport()) {
				ctx.movement.step(npc);
			}
		}

	}
}
