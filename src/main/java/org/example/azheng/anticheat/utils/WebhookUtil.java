package org.example.azheng.anticheat.utils;

import org.bukkit.Bukkit;
import org.example.azheng.anticheat.Anticheat;
import org.example.azheng.anticheat.config.ConfigObject;

import java.net.URI;
import java.net.http.*;
import java.time.Instant;
import java.util.LinkedList;
import java.util.Queue;

public class WebhookUtil {

    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    private static final Queue<String> QUEUE = new LinkedList<>();

    private static final int MAX_REQUESTS = 10;
    private static final int MAX_SEND = 5;

    private static final long LIMIT_TIME = 5000L;

    private static int requests;
    private static int sent;

    private static long limiterStart;

    private static boolean sending;


    /**
     * Sends a Discord webhook violation message.
     *
     * @param player Player name
     * @param check Check name
     * @param verbose Violation information
     */
    public static synchronized void sendViolation(String player, String check, String verbose) {

        ConfigObject config =
                Anticheat.instance.configLoader.getConfig("webhook.yml");

        if (config == null || !config.getBoolean("webhook.enabled")) {
            return;
        }


        long current = System.currentTimeMillis();


        // Reset limiter after the cooldown period.
        if (current - limiterStart >= LIMIT_TIME) {
            limiterStart = current;
            requests = 0;
            sent = 0;
        }


        // Ignore spam after 10 violations.
        if (requests >= MAX_REQUESTS) {
            return;
        }


        requests++;


        // Only send 5 webhooks out of the 10 violations.
        if (sent >= MAX_SEND) {
            return;
        }


        sent++;


        QUEUE.add(
                createPayload(
                        player,
                        check,
                        verbose
                )
        );

        processQueue();
    }


    private static synchronized void processQueue() {

        if (sending || QUEUE.isEmpty()) {
            return;
        }

        sending = true;


        Bukkit.getScheduler().runTaskAsynchronously(
                Anticheat.instance,
                () -> {

                    sendWebhook(
                            QUEUE.poll()
                    );


                    Bukkit.getScheduler().runTaskLater(
                            Anticheat.instance,
                            () -> {

                                synchronized (WebhookUtil.class) {
                                    sending = false;
                                    processQueue();
                                }

                            },
                            20L
                    );
                }
        );
    }


    /*
     * Due to discords limitations, we must avoid sending too many requests at the same time.
     * As far as I was Informed, discord can only accept 1 webhook per 1s before issuing the
     * sender a ratelimit. To avoid this we will restrict the amount sent, e.x:
     * PlayerA triggers 10 violations, out of those 10 only 5 webhooks are sent.
     */
    private static String createPayload(
            String player,
            String check,
            String verbose
    ) {

        ConfigObject config =
                Anticheat.instance.configLoader.getConfig("webhook.yml");


        String description = replace(
                config.getString("webhook.description"),
                player,
                check,
                verbose
        );


        return """
                {
                  "embeds": [{
                    "title": "%s",
                    "description": "%s",
                    "color": %d,
                    "thumbnail": {
                      "url": "%s"
                    },
                    "footer": {
                      "text": "%s"
                    },
                    "timestamp": "%s"
                  }]
                }
                """.formatted(
                escape(config.getString("webhook.title")),
                escape(description),
                Integer.parseInt(
                        config.getString("webhook.color")
                                .replace("#", ""),
                        16
                ),
                replace(
                        config.getString("webhook.thumbnail"),
                        player,
                        check,
                        verbose
                ),
                escape(
                        replace(
                                config.getString("webhook.footer"),
                                player,
                                check,
                                verbose
                        )
                ),
                Instant.now()
        );
    }


    private static void sendWebhook(String json) {

        ConfigObject config =
                Anticheat.instance.configLoader.getConfig("webhook.yml");

        String url = config.getString("webhook.url");

        if (url == null || url.isEmpty()) {
            return;
        }


        try {

            CLIENT.send(
                    HttpRequest.newBuilder()
                            .uri(URI.create(url))
                            .header(
                                    "Content-Type",
                                    "application/json"
                            )
                            .POST(
                                    HttpRequest.BodyPublishers.ofString(json)
                            )
                            .build(),
                    HttpResponse.BodyHandlers.ofString()
            );

        } catch (Exception e) {

            Anticheat.instance.getLogger()
                    .warning(
                            "Failed to send webhook: "
                                    + e.getMessage()
                    );
        }
    }


    private static String replace(
            String message,
            String player,
            String check,
            String verbose
    ) {

        if (message == null) {
            return "";
        }

        return message
                .replace("%player%", player)
                .replace("%check%", check)
                .replace("%verbose%", verbose)
                .replace("%timestamp%", Instant.now().toString());
    }


    private static String escape(String text) {

        return text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n");
    }
}