package org.qosmiof2.scripts.fighter.tasks;

import java.util.concurrent.Callable;

import org.powerbot.script.Condition;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Item;
import org.qosmiof2.scripts.fighter.gui.Gui;
import org.qosmiof2.scripts.framework.Node;

public class Eat extends Node {

	public Eat(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		return ctx.players.local().healthPercent() <= Gui.percent
				&& !ctx.backpack.select().name(Gui.selectedFood).isEmpty();
	}

	@Override
	public void execute() {
		Item item = ctx.backpack.name(Gui.selectedFood).shuffle().poll();

		if (item.interact("Attack", Gui.selectedFood)) {
			Condition.wait(new Callable<Boolean>() {

				@Override
				public Boolean call() throws Exception {
					return ctx.players.local().healthPercent() > Gui.percent;
				}

			}, 500, 5);
		}

	}

}
