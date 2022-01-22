package tk.wosaj.lambda.commands.normal;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import tk.wosaj.lambda.commands.Argument;
import tk.wosaj.lambda.commands.Command;
import tk.wosaj.lambda.commands.RegisterCommand;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
@RegisterCommand(byDefault = true)
public class TestCommand implements Command {
    @Override
    public boolean hasArguments() {
        return false;
    }

    @Override
    public Argument[] getArguments() {
        return new Argument[0];
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        event.getMessage().reply(new MessageBuilder().append("?test").build()).queue();
    }

    @Override
    public void executeAsSlash(SlashCommandEvent event) {
        event.reply("sus").queue();
    }

    @Nonnull
    @Override
    public String getName() {
        return "test";
    }

    @Nonnull
    @Override
    public String getDescription() {
        return "sus";
    }
}
