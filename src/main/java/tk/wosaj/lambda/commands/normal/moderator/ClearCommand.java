package tk.wosaj.lambda.commands.normal.moderator;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.jetbrains.annotations.NotNull;
import tk.wosaj.lambda.commands.Argument;
import tk.wosaj.lambda.commands.Command;
import tk.wosaj.lambda.commands.RegisterCommand;
import tk.wosaj.lambda.setup.Main;

import java.util.Objects;

import static tk.wosaj.lambda.commands.CommandManager.splitCommand;
import static tk.wosaj.lambda.commands.Commands.reply;

@SuppressWarnings("unused")
@RegisterCommand(byDefault = true)
public class ClearCommand implements Command {
    @Override
    public boolean hasArguments() {
        return true;
    }

    @Override
    public Argument[] getArguments() {
        return new Argument[]{
                new Argument(
                        OptionType.INTEGER,
                        "messages",
                        "Number of messages",
                        false
                )
        };
    }

    @Override
    public void execute(MessageReceivedEvent event) {

        if(!Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR)) {
            reply(event.getMessage(), "<:canceled:934419252495130744> You can't use it!");
            return;
        }

        String[] split = splitCommand(event.getMessage().getContentRaw(), Main.defaultPrefix);
        try {
            int messages = Integer.parseInt(split[1]);
            if(messages <= 0) throw new IllegalArgumentException();
            event.getTextChannel().getHistory().retrievePast(messages).queue(list ->
            event.getTextChannel().deleteMessages(list).queue());
            reply(event.getMessage(), "<:accepted:911605951335915530> Deleted " + messages + " messages!");
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            reply(event.getMessage(), "<:canceled:934419252495130744> Enter number of messages!");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            reply(event.getMessage(), "<:canceled:934419252495130744> Invalid number of messages!");
        } catch (Exception e) {
            e.printStackTrace();
            reply(event.getMessage(), "<:canceled:934419252495130744> Unknown error!");
        }
    }

    @Override
    public void executeAsSlash(SlashCommandEvent event) {
        try {
            int messages = (int) Objects.requireNonNull(event.getOption("messages")).getAsLong();
            if(messages <= 0) throw new IllegalArgumentException();
            event.getTextChannel().getHistory().retrievePast(messages).queue(list ->
                    event.getTextChannel().deleteMessages(list).queue());
            event.reply("<:accepted:911605951335915530> Deleted " + messages + " messages!")
                    .setEphemeral(true).queue();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            event.reply("<:canceled:934419252495130744> Invalid number of messages!").queue();
        } catch (Exception e) {
            e.printStackTrace();
            event.reply("<:canceled:934419252495130744> Unknown error!").queue();
        }
    }

    @NotNull
    @Override
    public String getName() {
        return "clear";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Clear number of messages";
    }
}
