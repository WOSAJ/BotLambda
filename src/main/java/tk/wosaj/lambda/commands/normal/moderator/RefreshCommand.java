package tk.wosaj.lambda.commands.normal.moderator;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import tk.wosaj.lambda.commands.Argument;
import tk.wosaj.lambda.commands.Command;
import tk.wosaj.lambda.commands.RegisterCommand;
import tk.wosaj.lambda.setup.Main;

import javax.annotation.Nonnull;
import java.util.Objects;

import static tk.wosaj.lambda.commands.Commands.reply;

@SuppressWarnings("unused")
@RegisterCommand(byDefault = true)
public class RefreshCommand implements Command {
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
        if(Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR)) {
            Main.manager.refresh(event.getGuild());
            reply(event.getMessage(), "<:accepted:911605951335915530> Success refreshed!");
        } else {
            reply(event.getMessage(), "<:canceled:934419252495130744> You can't use it!");
        }
    }

    @Override
    public void executeAsSlash(SlashCommandEvent event) {
        if(Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR)) {
            Main.manager.refresh(Objects.requireNonNull(event.getGuild()));
            event.reply("<:accepted:911605951335915530> Success refreshed!").queue();
        } else {
            event.reply("<:canceled:934419252495130744> You can't use it!").queue();
        }
    }

    @Nonnull
    @Override
    public String getName() {
        return "refresh";
    }

    @Nonnull
    @Override
    public String getDescription() {
        return "Reregister all command on this guild";
    }
}
