package org.qosmiof2.scripts.fighter.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.powerbot.script.rt6.ClientAccessor;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GroundItem;
import org.powerbot.script.rt6.Item;
import org.powerbot.script.rt6.Npc;
import org.qosmiof2.scripts.fighter.Fighter;
import org.qosmiof2.scripts.fighter.tasks.Attack;
import org.qosmiof2.scripts.fighter.tasks.Eat;
import org.qosmiof2.scripts.fighter.tasks.Loot;
import org.qosmiof2.scripts.fighter.tasks.MoveMouse;

public class Gui extends ClientAccessor {

	public Gui(ClientContext ctx) {
		super(ctx);
		init();

	}

	private JFrame frame = new JFrame("QAIO Fighter");
	private DefaultListModel<String> model = new DefaultListModel<>();
	private DefaultListModel<String> modelFood = new DefaultListModel<>();
	private ArrayList<String> listNpc = new ArrayList<>();
	private ArrayList<String> listFood = new ArrayList<>();
	private JList<String> list = new JList<>(model);
	private JList<String> listEat = new JList<>(modelFood);
	private JButton button, button1;
	private JTabbedPane tabbedPane = new JTabbedPane();
	private JScrollPane sPane = new JScrollPane(list);
	private JScrollPane sPaneEat = new JScrollPane(listEat);
	private JLabel percentLabel, selectNpcLabel;
	private JPanel panel = new JPanel();
	private JPanel eatPanel = new JPanel();
	private JPanel startPanel = new JPanel();
	private JLabel labelNpc, labelFood, labelHealth;
	private double percent;
	private String selected;
	private String selectedFood;
	private String[] npcs;
	private JCheckBox checkBoxEat = new JCheckBox("Eat food");
	private SpinnerNumberModel spinnerModel = new SpinnerNumberModel(99.0,
			10.0, 99.0, 1.0);
	private JSpinner spinnerPercent = new JSpinner(spinnerModel);
	private JButton startButton = new JButton("Start");
	private ArrayList<String> npcToAttack = new ArrayList<>();
	private DefaultListModel<String> npcsToAttackModel = new DefaultListModel<>();
	private JList<String> npcsToAttackList = new JList<>(npcsToAttackModel);
	private JScrollPane npcsToAttackPane = new JScrollPane(npcsToAttackList);
	private JButton addButton = new JButton("Add");

	private JPanel panelLoot = new JPanel();
	private DefaultListModel<String> lootModel = new DefaultListModel<>();
	private DefaultListModel<String> lootSelectedModel = new DefaultListModel<>();
	private JList<String> lootList = new JList<>(lootModel);
	private JList<String> lootListSelected = new JList<>(lootSelectedModel);
	private JScrollPane lootPane = new JScrollPane(lootList);
	private JScrollPane lootPaneSelected = new JScrollPane(lootListSelected);
	private JButton buttonAdd = new JButton("Add");
	private JButton buttonRefresh = new JButton("Refresh");
	private ArrayList<String> lootListArray = new ArrayList<>();
	private ArrayList<String> lootListSelectedArray = new ArrayList<>();
	private JLabel labelLoot, labelLootSelected;
	private String selectedLoot;
	private String[] lootSelected;
	private JTextField lootName = new JTextField("Loot name");

	private void init() {

		ctx.input.blocking(false);
		lootPanel();
		panel();
		eatPanel();
		startPanel();

		frame.setLocationRelativeTo(null);
		frame.setSize(new Dimension(300, 260));
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
		frame.add(tabbedPane);

		tabbedPane.setFocusable(false);
		tabbedPane.add("Target", panel);
		tabbedPane.add("Food", eatPanel);
		tabbedPane.add("Loot", panelLoot);
		tabbedPane.add("Start", startPanel);

		tabbedPane.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				updateSelectedItems();
			}
		});

	}

	private void panel() {
		selectNpcLabel = new JLabel("Please select target:");
		button = new JButton("Refresh");

		panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		panel.add(selectNpcLabel);
		panel.add(sPane);
		panel.add(npcsToAttackPane);
		panel.add(button);
		panel.add(Box.createRigidArea(new Dimension(20, 0)));
		panel.add(addButton);

		selectNpcLabel.setPreferredSize(new Dimension(200, 20));

		addButton.setPreferredSize(new Dimension(80, 30));
		addButton.setFocusable(false);

		npcsToAttackPane.setPreferredSize(new Dimension(100, 120));
		npcsToAttackList.setEnabled(false);
		npcsToAttackPane.setBorder(BorderFactory.createEtchedBorder());
		npcsToAttackPane.setFocusable(false);
		;

		sPane.setPreferredSize(new Dimension(100, 120));
		sPane.setBorder(BorderFactory.createEtchedBorder());
		sPane.setFocusable(false);

		button.setPreferredSize(new Dimension(80, 30));
		button.setFocusable(false);

		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				listNpc.clear();
				model.clear();
				for (Npc npc : ctx.npcs.select()) {
					if (!listNpc.contains(npc.name())) {
						listNpc.add(npc.name());
					}
				}

				for (String listString : listNpc) {
					model.addElement(listString);
				}
				selected = "null";
			}

		});

		list.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent arg1) {
				selected = list.getSelectedValue();
			}
		});

		addButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				if (!npcToAttack.contains(selected) && !selected.equals(null)) {
					npcToAttack.add(selected);
				}

				for (String string : npcToAttack) {
					if (!npcsToAttackModel.contains(string)) {
						npcsToAttackModel.addElement(string);
					}

				}

			}

		});
	}

	private void eatPanel() {

		button1 = new JButton("Refresh");
		percentLabel = new JLabel("Health percent to eat at");

		eatPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		eatPanel.add(checkBoxEat);
		eatPanel.add(percentLabel);
		eatPanel.add(spinnerPercent);
		eatPanel.add(sPaneEat);
		eatPanel.add(button1);

		sPaneEat.setEnabled(false);
		button1.setEnabled(false);
		spinnerPercent.setEnabled(false);
		percentLabel.setEnabled(false);

		sPaneEat.setPreferredSize(new Dimension(100, 150));
		sPaneEat.setBorder(BorderFactory.createEtchedBorder());
		sPaneEat.setFocusable(false);

		checkBoxEat.setPreferredSize(new Dimension(80, 20));
		checkBoxEat.setFocusable(false);

		spinnerPercent.setPreferredSize(new Dimension(50, 20));
		spinnerPercent.setFocusable(false);

		button1.setPreferredSize(new Dimension(80, 30));
		button1.setFocusable(false);

		button1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				listFood.clear();
				modelFood.clear();
				for (Item item : ctx.backpack.select()) {
					if (!listFood.contains(item.name())) {
						listFood.add(item.name());
					}
				}

				for (String listStringFood : listFood) {
					modelFood.addElement(listStringFood);
				}

				selectedFood = "null";
			}

		});

		listEat.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				selectedFood = listEat.getSelectedValue();

			}

		});

		checkBoxEat.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (checkBoxEat.isSelected()) {
					button1.setEnabled(true);
					listEat.setEnabled(true);
					spinnerPercent.setEnabled(true);
					percentLabel.setEnabled(true);
				} else {
					button1.setEnabled(false);
					listEat.setEnabled(false);
					spinnerPercent.setEnabled(false);
					percentLabel.setEnabled(false);
				}

			}

		});

		spinnerPercent.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				percent = (double) spinnerPercent.getValue();

			}

		});

		spinnerPercent.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent kEvent) {
				percent = (double) spinnerPercent.getValue();
			}
		});
	}

	private void startPanel() {

		labelNpc = new JLabel("Selected target: ");
		labelFood = new JLabel("Selected food: ");
		labelLootSelected = new JLabel("Selected loot: ");
		labelHealth = new JLabel("Health percent: ");

		startPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		startPanel.add(labelNpc);
		startPanel.add(labelFood);
		startPanel.add(labelLootSelected);
		startPanel.add(labelHealth);
		startPanel.add(startButton);

		labelNpc.setPreferredSize(new Dimension(300, 20));
		labelFood.setPreferredSize(new Dimension(300, 20));
		labelHealth.setPreferredSize(new Dimension(300, 20));
		labelLootSelected.setPreferredSize(new Dimension(300, 20));

		startButton.setPreferredSize(new Dimension(100, 30));
		startButton.setFocusable(false);

		startButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				npcs = npcToAttack.toArray(new String[npcToAttack.size()]);
				lootSelected = lootListSelectedArray
						.toArray(new String[lootListSelectedArray.size()]);
				Fighter.nodeList.add(new Attack(ctx, Gui.this));
				Fighter.nodeList.add(new MoveMouse(ctx));
				if (checkBoxEat.isSelected()) {
					Fighter.nodeList.add(new Eat(ctx, Gui.this));
				}
				if (lootSelected.length != 0) {
					Fighter.nodeList.add(new Loot(ctx, Gui.this));
				}
				System.out.println(lootSelected.length);
				frame.dispose();
				ctx.input.blocking(true);

			}

		});

	}

	private void updateSelectedItems() {
		labelNpc.setText("Selected target: " + npcToAttack + ".");
		if (checkBoxEat.isSelected()) {
			labelFood.setText("Selected food: " + selectedFood + ".");
		} else {
			labelFood.setText("Selected food: Not going to eat.");
		}
		labelLootSelected.setText("Selected loot: " + lootListSelectedArray);
		labelHealth.setText("Health percent: " + percent + ".");
	}

	private void lootPanel() {
		labelLoot = new JLabel("Please select loot:");

		panelLoot.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelLoot.add(labelLoot);
		panelLoot.add(lootName);
		panelLoot.add(lootPane);
		panelLoot.add(lootPaneSelected);
		panelLoot.add(buttonRefresh);
		panelLoot.add(Box.createRigidArea(new Dimension(20, 0)));
		panelLoot.add(buttonAdd);

		labelLoot.setPreferredSize(new Dimension(100, 20));
		lootName.setPreferredSize(new Dimension(100, 20));

		buttonAdd.setPreferredSize(new Dimension(80, 30));
		buttonAdd.setFocusable(false);

		lootPaneSelected.setPreferredSize(new Dimension(100, 120));
		lootPaneSelected.setEnabled(false);
		lootPaneSelected.setBorder(BorderFactory.createEtchedBorder());
		lootPaneSelected.setFocusable(false);

		lootListSelected.setEnabled(false);
		lootPane.setPreferredSize(new Dimension(100, 120));
		lootPane.setBorder(BorderFactory.createEtchedBorder());
		lootPane.setFocusable(false);

		buttonRefresh.setPreferredSize(new Dimension(80, 30));
		buttonRefresh.setFocusable(false);

		buttonRefresh.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				lootListArray.clear();
				lootModel.clear();
				for (GroundItem item : ctx.groundItems.select()) {
					if (!lootListArray.contains(item.name())) {
						lootListArray.add(item.name());
					}
				}

				for (String listString : lootListArray) {
					lootModel.addElement(listString);
				}
			}

		});

		lootList.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent arg1) {
				selectedLoot = lootList.getSelectedValue();
			}
		});

		buttonAdd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				if (!lootListSelectedArray.contains(selectedLoot)) {
					lootListSelectedArray.add(selectedLoot);
				}

				if (!lootListSelectedArray.contains(lootName.getText())
						&& !lootName.getText().equals("Loot name")
						&& !lootName.getText().equals("")) {
					lootListSelectedArray.add(lootName.getText());
					lootSelectedModel.addElement(lootName.getText());
				}

				for (String string : lootListSelectedArray) {
					if (!lootSelectedModel.contains(string)) {
						lootSelectedModel.addElement(string);
					}

				}

			}

		});

	}

	public double getPercent() {
		return percent;
	}

	public String[] getNpcs() {
		return npcToAttack.toArray(new String[npcToAttack.size()]);
	}

	public String[] getLootSelected() {
		return lootListSelectedArray.toArray(new String[lootListSelectedArray
				.size()]);
	}

	public String getSelectedFood() {
		return selectedFood;
	}

}
