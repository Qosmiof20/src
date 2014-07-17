package org.qosmiof2.scripts.fighter.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Npc;
import org.qosmiof2.scripts.fighter.gui.Gui;
import org.qosmiof2.scripts.framework.Node;

public class Attack extends Node {

	public Attack(ClientContext ctx) {
		super(ctx);
	}

	private Filter<Npc> filter = new Filter<Npc>() {

		public boolean accept(Npc npc) {
			if (!npc.inCombat() && !npc.interacting().valid()) {
				return true;
			}
			return false;
		}

	};

	@Override
	public boolean activate() {

		Npc npc = ctx.npcs.select().select(filter).name(Gui.npcs).nearest()
				.first().poll();

		return !ctx.players.local().interacting().valid()
				&& ctx.players.local().animation() == -1
				&& !ctx.players.local().inMotion() && npc.valid();
	}

	@Override
	public void execute() {
		Npc npc = ctx.npcs.select().select(filter).name(Gui.npcs).nearest()
				.first().poll();

		if (npc.inViewport()) {

			if (npc.interact("Attack")) {

				while (ctx.players.local().inMotion()) {
					Condition.sleep();
				}

			}
		} else {
			ctx.camera.turnTo(npc);
		}

	}
}
