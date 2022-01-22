package tk.wosaj.lambda.commands.normal;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.jetbrains.annotations.NotNull;
import tk.wosaj.lambda.commands.Argument;
import tk.wosaj.lambda.commands.Command;
import tk.wosaj.lambda.commands.CommandManager;
import tk.wosaj.lambda.commands.RegisterCommand;
import tk.wosaj.lambda.setup.Main;

import java.util.Objects;

import static tk.wosaj.lambda.commands.Commands.reply;

//REFACTOR database unregister
@SuppressWarnings("unused")
@RegisterCommand(byDefault = true)
public class DeleteCommand implements Command {
    @Override
    public boolean hasArguments() {
        return true;
    }

    @Override
    public Argument[] getArguments() {
        return new Argument[] {
            new Argument(
                    OptionType.STRING,
                    "name",
                    "Command name",
                    false
            )
        };
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        if(Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR)) {
            try {
                String[] split =
                        CommandManager.splitCommand(event.getMessage().getContentRaw(), Main.defaultPrefix);
                        Main.manager.removeCommand(Main.manager.getForName(split[1]), event.getGuild());
                        reply(event.getMessage(), "<:accepted:911605951335915530> Success deleted!");
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
                reply(event.getMessage(), "<:canceled:934419252495130744> No arguments!");
            }
        } else reply(event.getMessage(), "<:canceled:934419252495130744> You can't use it!");
    }

    @Override
    public void executeAsSlash(SlashCommandEvent event) {
        if(Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR)) {
            if (event.getGuild() == null) {
                event.reply("Now????").queue();
                return;
            }
                String name = Objects.requireNonNull(event.getOption("name")).getAsString();
            Main.manager.removeCommand(Main.manager.getForName(name), event.getGuild());
            event.reply("<:accepted:911605951335915530> Success deleted!").queue();
        } else event.reply("<:canceled:934419252495130744> You can't use it!").queue();
    }

    @NotNull
    @Override
    public String getName() {
        return "delete";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Delete command by name";
    }
}
