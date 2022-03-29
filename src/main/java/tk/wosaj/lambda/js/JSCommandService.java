package tk.wosaj.lambda.js;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.dv8tion.jda.api.requests.restaction.WebhookMessageAction;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import tk.wosaj.lambda.Main;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class JSCommandService {
    public static final String downloadPrefix;
    static {
        Properties properties = new Properties();
        try {
            properties.load(Main.loader.getReader("properties/data.properties"));
        } catch (IOException ignored) {}
        downloadPrefix = properties.getProperty("js.download.prefix", "js_download");
    }

    short replyCount = 0;
    @Nullable
    private final Message message;
    @Nullable
    private final SlashCommandInteractionEvent event;
    private boolean ephemeral;

    JSCommandService(@Nonnull Message message) {
        this.message = message;
        this.event = null;
        this.ephemeral = false;
    }

    JSCommandService(@Nonnull SlashCommandInteractionEvent event, boolean ephemeral) {
        this.message = null;
        this.event = event;
        this.ephemeral = ephemeral;
    }

    public void reply(String text) {
        if (replyCount >= 3) throw new RuntimeException("Reply limit reached");
        try {
            if (event != null) {
                event.getHook().sendMessage(text).setEphemeral(ephemeral).queue();
            } else if (message != null) {
                message.reply(text).queue();
            }
        } finally {
            replyCount++;
        }
    }

    public JSBuilder builder(String initialContent) {
        return event != null ? new SlashReplyBuilder(initialContent) : new ReplyBuilder(initialContent);
    }

    public String[] getAttachments() {
        return message != null
                ? message.getAttachments().stream().map(Message.Attachment::getUrl).toArray(String[]::new)
                : new String[0];
    }

    @Nullable
    Message getMessage() {
        return message;
    }

    @Nullable
    SlashCommandInteractionEvent getEvent() {
        return event;
    }

    public boolean isEphemeral() {
        return ephemeral;
    }

    public static abstract class JSBuilder {
        public abstract JSBuilder setContent(String content);
        public abstract JSBuilder appendContent(String content);
        public abstract JSBuilder plusContent(String content);
        abstract void embed(MessageEmbed embed);
        public abstract JSBuilder addFile(String url, String ext);
        public abstract void reply();
        public JSBuilder setEphemeral(boolean value) {
            return this;
        }
    }

    public class ReplyBuilder extends JSBuilder {
        public String content;
        private short fileCount = 0;
        private MessageEmbed embed = null;
        private final Map<String, String> fileList = new HashMap<>();

        ReplyBuilder(String content) {
            this.content = content;
        }

        @Override
        public ReplyBuilder setContent(String content) {
            this.content = content;
            return this;
        }

        @Override
        public ReplyBuilder appendContent(String text) {
            content += ('\n' + text);
            return this;
        }

        @Override
        public ReplyBuilder plusContent(String text) {
            content += text;
            return this;
        }

        @Override
        void embed(MessageEmbed embed) {
            this.embed = embed;
        }

        @Override
        public ReplyBuilder addFile(String url, String ext) {
            if(fileCount >= 10) throw new RuntimeException("File limit reached");

            if(message != null) {
                fileList.put(ext, url);
                fileCount++;
            }
            return this;
        }

        @Override
        public void reply() {
            if (message == null) return;
            if (replyCount >= 3) throw new RuntimeException("Reply limit reached");
            try {
                MessageAction reply;
                if (embed == null) {
                    reply = message.reply(content);
                    if (!fileList.isEmpty()) {
                        List<File> collect = fileList.entrySet().stream().map(url -> {
                            try {
                                OkHttpClient client = new OkHttpClient.Builder().build();
                                Response execute = client.newCall(new Request.Builder().url(url.getValue()).build()).execute();
                                assert execute.body() != null;
                                byte[] bytes = execute.body().bytes();
                                File tempFile = File.createTempFile("js_download", "attachment" + url.getKey());
                                FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
                                fileOutputStream.write(bytes);
                                fileOutputStream.flush();
                                fileOutputStream.close();
                                execute.body().close();
                                execute.close();
                                return tempFile;
                            } catch (Exception e) {
                                message.reply("Exception in your code!\n" + e.getMessage()).queue();
                            }
                            return null;
                        }).collect(Collectors.toList());
                        for (File file : collect) {
                            try {
                                if (file != null) {
                                    reply = reply.addFile(file, file.getName());
                                    file.deleteOnExit();
                                }
                            } catch (Exception e) {
                                message.reply("Exception in your code!\n" + e.getMessage()).queue();
                            }
                        }
                        reply.queue(m -> collect.forEach(f -> {
                            try {
                                Files.delete(f.toPath());
                            } catch (Exception ignored) {}
                        }));
                        return;
                    }
                } else {
                    reply = message.replyEmbeds(embed);
                    if(!content.equals("")) reply = reply.content(content);
                }
                reply.queue();
            } finally {
                replyCount++;
            }
        }
    }

    public class SlashReplyBuilder extends JSBuilder {
        public String content;
        MessageEmbed embed;
        private short fileCount = 0;
        private final Map<String, String> fileList = new HashMap<>();

        public SlashReplyBuilder(String content) {
            this.content = content;
        }

        @Override
        public SlashReplyBuilder setContent(String content) {
            this.content = content;
            return this;
        }

        @Override
        public SlashReplyBuilder appendContent(String text) {
            content += ('\n' + text);
            return this;
        }

        @Override
        public SlashReplyBuilder plusContent(String text) {
            content += text;
            return this;
        }

        @Override
        public SlashReplyBuilder addFile(String url, String ext) {
            if(fileCount >= 10) throw new RuntimeException("File limit reached");

            if(message != null) {
                fileList.put(ext, url);
                fileCount++;
            }
            return this;
        }

        @Override
        public SlashReplyBuilder setEphemeral(boolean ephemeral_) {
            ephemeral = ephemeral_;
            return this;
        }

        @Override
        void embed(MessageEmbed embed) {
            this.embed = embed;
        }

        @Override
        public void reply() {
            if (event == null) return;
            if (replyCount > 0) throw new RuntimeException("Reply limit reached");
            try {
                WebhookMessageAction<Message> reply = event.getHook().sendMessage(content);
                if (!fileList.isEmpty()) {
                    List<File> collect = fileList.entrySet().stream().map(url -> {
                        try {
                            OkHttpClient client = new OkHttpClient.Builder().build();
                            Response execute = client.newCall(new Request.Builder().url(url.getValue()).build()).execute();
                            assert execute.body() != null;
                            byte[] bytes = execute.body().bytes();
                            File tempFile = File.createTempFile("js_download", "attachment" + url.getKey());
                            FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
                            fileOutputStream.write(bytes);
                            fileOutputStream.flush();
                            fileOutputStream.close();
                            execute.body().close();
                            execute.close();
                            return tempFile;
                        } catch (Exception e) {
                            event.getHook().sendMessage("Exception in your code!\n" + e.getMessage())
                                    .setEphemeral(true).queue();
                        }
                        return null;
                    }).collect(Collectors.toList());
                    if(!ephemeral) for (File file : collect) {
                        try {
                            if (file != null) {
                                reply = reply.addFile(file, file.getName());
                                file.deleteOnExit();
                            }
                        } catch (Exception e) {
                            event.getHook().sendMessage("Exception in your code!\n" + e.getMessage())
                                    .setEphemeral(true).queue();
                        }
                    }reply.setEphemeral(ephemeral).queue(m -> collect.forEach(f -> {
                        try {
                            Files.delete(f.toPath());
                        } catch (Exception ignored) {}
                    }));
                    return;
                }
                if(embed == null) reply.setEphemeral(ephemeral).queue();
                else reply.setEphemeral(ephemeral).addEmbeds(embed).queue();
            } finally {
                replyCount++;
            }
        }
    }
}