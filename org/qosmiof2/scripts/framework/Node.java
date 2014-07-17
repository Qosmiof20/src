package org.qosmiof2.scripts.framework;

import org.powerbot.script.rt6.ClientAccessor;
import org.powerbot.script.rt6.ClientContext;

public abstract class Node extends ClientAccessor{

	public Node(ClientContext ctx) {
		super(ctx);
	}

	public abstract boolean activate();
	
	public abstract void execute();
}
