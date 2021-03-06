package de.ovgu.dke.xmpp.echo;

import java.io.IOException;
import java.net.URI;
import java.util.Properties;

import de.ovgu.dke.mocca.api.MoccaException;
import de.ovgu.dke.mocca.api.MoccaRuntime;
import de.ovgu.dke.mocca.api.command.Command;
import de.ovgu.dke.mocca.api.context.Context;
import de.ovgu.dke.mocca.util.MoccaHelper;

public class EchoSender {
	public static final URI echoCmd = URI
			.create("http://dke.ovgu.de/mocca/test/command/echo");

	public static void main(String[] args) throws MoccaException, IOException {

		final MoccaRuntime mocca = MoccaHelper.getDefaultRuntime();

		Properties env = new Properties();
		env.setProperty("de.ovgu.dke.glue.xmpp.server", "Your Server");
		env.setProperty("de.ovgu.dke.glue.xmpp.user", "Your User");
		env.setProperty("de.ovgu.dke.glue.xmpp.pass", "Your Password");
		// and so on, all the values from the configuration file may go here.

		// leave this out to actually use the above configuration
		env = MoccaRuntime.NULL_ENV;

		mocca.init(env);

		mocca.getCommandHandlerRegistry().registerCommandHandler(
				new EchoCommandHandler());

		// create a context
		final Context ctx = mocca.createContext();
		ctx.connect(URI.create("xmpp:shaun@bison.cs.uni-magdeburg.de"));

		// create a command
		final Properties props = new Properties();
		props.put("text", "Hallo Welt!");
		final Command cmd = mocca.createCommand(EchoCommandHandler.CMD_ECHO,
				props);

		// send the command
		ctx.sendCommand(cmd);

		System.in.read();

		// disconnect the context
		ctx.disconnect();

		mocca.dispose();
	}
}
