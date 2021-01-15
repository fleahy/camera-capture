package org.vaadin.gilberto;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Synchronize;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.internal.JsonSerializer;

@Tag("camera-capture")
@JsModule("./src/camera-capture.js")
public class CameraCapture extends Component implements HasSize, HasStyle {

    private boolean playing;
    private final Map<String, SerializableConsumer<String>> callbacks;

    public CameraCapture() {
        callbacks = new HashMap<>();
        play();
    }

    public CameraCapture(String width, String height) {
        this();
        setHeight(height);
        setWidth(width);
    }

    public void play() {
        playing = true;
        getElement().callJsFunction("play");
    }

    public void stop() {
        playing = false;
        getElement().callJsFunction("stop");
    }

    public void pause() {
        playing = false;
        getElement().callJsFunction("pause");
    }

    @Override
    @Synchronize("height-changed")
    public String getHeight() {
        return getElement().getProperty("height");
    }

    @Override
    public void setHeight(String height) {
        getElement().setProperty("height", height);
    }

    @Override
    @Synchronize("width-changed")
    public String getWidth() {
        return getElement().getProperty("width");
    }

    @Override
    public void setWidth(String width) {
        getElement().setProperty("width", width);
    }

    public boolean isPlaying() {
        return playing;
    }

    public void capture(SerializableConsumer<String> callback) {
        capture("image/png", callback);
    }

    public void capture(String mimeType,
            SerializableConsumer<String> callback) {
        String captureId = UUID.randomUUID().toString();
        callbacks.put(captureId, callback);
        getElement().callJsFunction("_captureAndSendToServer", captureId,
                mimeType);
    }

    public void setOverlay(OverlayStyle overlay) {
        setOverlayVisible(true);
        getElement().setPropertyJson("overlayStyle",
                JsonSerializer.toJson(overlay));
    }

    @Synchronize("overlay-hidden-changed")
    public boolean isOverlayVisible() {
        return getElement().getProperty("overlayHidden", false);
    }

    public void setOverlayVisible(boolean visible) {
        getElement().setProperty("overlayHidden", !visible);
    }

    @ClientCallable
    private void onImageCaptured(String captureId, String data) {
        SerializableConsumer<String> callback = callbacks.remove(captureId);
        if (callback == null) {
            return;
        }
        callback.accept(data);
    }

}
