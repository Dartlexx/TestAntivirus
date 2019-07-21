package com.dartlexx.eicarscanner.misc;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

import com.dartlexx.eicarscanner.R;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public final class MiscHelper {

    private static final int BUFFER_SIZE = 256;

    private MiscHelper() {
    }

    public static Drawable getAppShieldIcon(@NonNull Context context) {
        try (InputStream stream = context.getAssets().open("appShieldIcon.bmp")) {
            return Drawable.createFromStream(stream, null);
        } catch (IOException exc) {
            return null;
        }
    }

    public static void showDialogWithInfo(@NonNull Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle(context.getString(R.string.dialog_title_app_info))
                .setMessage(getMessage(context))
                .create()
                .show();
    }

    /**
     * To hide secret string I implemented some simplified steganography algorithm.
     * <p>
     * Data bit is stored at every 256 byte, beginning from the file start.
     * First 8 data bits define a length of the following UTF-8 string.
     */
    @SuppressWarnings("IllegalCatch")
    private static String getMessage(@NonNull Context context) {
        try (InputStream stream = context.getAssets().open("appShieldIcon.bmp")) {
            byte[] buffer = new byte[BUFFER_SIZE];
            byte length = 0;

            // Read length of secret string (just 1 byte for simplicity)
            for (int i = 0; i < 8; i++) {
                if (stream.read(buffer) != BUFFER_SIZE) {
                    throw new IllegalStateException("Failed to read from file");
                }
                byte lengthBit = (byte) (buffer[BUFFER_SIZE - 1] & 0x01);
                length |= lengthBit << i;
            }

            byte[] secret = new byte[length];
            int bitIndex = 0;
            while (stream.read(buffer) == BUFFER_SIZE) {
                byte dataBit = (byte) (buffer[BUFFER_SIZE - 1] & 0x01);
                secret[bitIndex >> 3] |= dataBit << (bitIndex & 0x07);

                bitIndex++;
                if (bitIndex >= length << 3) {
                    return new String(secret, StandardCharsets.UTF_8);
                }
            }
        } catch (Throwable ignored) {
        }
        return context.getString(R.string.app_name);
    }
}
