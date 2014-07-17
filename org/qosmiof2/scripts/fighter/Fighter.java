package org.qosmiof2.scripts.fighter;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import org.powerbot.script.Condition;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Random;
import org.powerbot.script.Script.Manifest;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Skills;
import org.qosmiof2.scripts.fighter.gui.Gui;
import org.qosmiof2.scripts.framework.Node;

@Manifest(description = "AIO Fighter - by Qosmiof2", name = "QAIOF")
public class Fighter extends PollingScript<ClientContext> implements
		PaintListener {

	public static ArrayList<Node> nodeList = new ArrayList<Node>();

	@Override
	public void start() {
		startExp = (ctx.skills.experience(Skills.CONSTITUTION)
				+ ctx.skills.experience(Skills.ATTACK)
				+ ctx.skills.experience(Skills.DEFENSE)
				+ ctx.skills.experience(Skills.STRENGTH)
				+ ctx.skills.experience(Skills.RANGE) + ctx.skills
				.experience(Skills.MAGIC));

		exp = startExp;
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new Gui(ctx);

			}

		});
	}

	@Override
	public void poll() {
		for (Node node : nodeList) {
			if (node.activate()) {
				node.execute();
				Condition.sleep(Random.nextInt(100, 500));
			}

		}

	}

	private String runTime() {
		final int sec = (int) (ctx.controller.script().getTotalRuntime() / 1000), h = sec / 3600, m = sec / 60 % 60, s = sec % 60;
		return (h < 10 ? "0" + h : h) + ":" + (m < 10 ? "0" + m : m) + ":"
				+ (s < 10 ? "0" + s : s);
	}

	private final Font font = new Font("Arial", 1, 13);
	private final Color color = new Color(0, 0, 0, 180);

	private int startExp, exp, expNow, kills;
	private int killsPerHour, expPerHour, expGained;

	@Override
	public void repaint(Graphics g1) {
		expNow = (ctx.skills.experience(Skills.CONSTITUTION)
				+ ctx.skills.experience(Skills.ATTACK)
				+ ctx.skills.experience(Skills.DEFENSE)
				+ ctx.skills.experience(Skills.STRENGTH)
				+ ctx.skills.experience(Skills.RANGE) + ctx.skills
				.experience(Skills.MAGIC));
		expGained = (expNow - startExp);
		killsPerHour = ((int) ((kills) * 3600000D / (ctx.controller.script()
				.getTotalRuntime())));

		expPerHour = ((int) ((expGained) * 3600000D / (ctx.controller.script()
				.getTotalRuntime())));

		if (expNow > exp) {
			kills++;
			exp = expNow;
		}
		Graphics2D g = (Graphics2D) g1;

		g.setColor(color);
		g.fillRect(5, 5, 200, 130);

		g.setColor(Color.WHITE);
		g.setFont(font);
		g.drawString(runTime(), 30, 30);
		g.drawString("Kills: " + kills, 30, 50);
		g.drawString("Kills p/h: " + killsPerHour, 30, 70);
		g.drawString("Exp gained: " + expGained, 30, 90);
		g.drawString("Exp p/h: " + expPerHour, 30, 110);

	}
}
