package org.qosmiof2.scripts.fighter.tasks;

import java.util.concurrent.Callable;

import org.powerbot.script.Condition;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Item;
import org.qosmiof2.scripts.fighter.gui.Gui;
import org.qosmiof2.scripts.framework.Node;

public class Eat extends Node {

	private Gui gui;

	public Eat(ClientContext ctx, Gui gui) {
		super(ctx);
		this.gui = gui;
	}

	@Override
	public boolean activate() {
		return ctx.players.local().healthPercent() <= gui.getPercent()
				&& !ctx.backpack.select().name(gui.getSelectedFood()).isEmpty();
	}

	@Override
	public void execute() {
		Item item = ctx.backpack.name(gui.getSelectedFood()).shuffle().poll();

		if (item.interact("Eat", gui.getSelectedFood())) {
			Condition.wait(new Callable<Boolean>() {

				@Override
				public Boolean call() throws Exception {
					return ctx.players.local().healthPercent() > gui
							.getPercent();
				}

			}, 500, 5);
		}

	}

}
