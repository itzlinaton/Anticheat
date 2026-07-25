package org.example.azheng.anticheat.config;

public class Placeholders {

    /**
     * Replaces placeholders inside a message.
     *
     * @param message The message to modify.
     * @param placeholders Placeholder pairs.
     *
     * @return The formatted message.
     */
    public static String replace(String message, Object... placeholders) {

        if (message == null || placeholders.length == 0) {
            return message;
        }

        if (placeholders.length % 2 != 0) {
            return message;
        }

        for (int i = 0; i < placeholders.length; i += 2) {

            String placeholder = String.valueOf(placeholders[i]);
            String value = String.valueOf(placeholders[i + 1]);

            message = message.replace(
                    placeholder,
                    value
            );
        }

        return message;
    }
}