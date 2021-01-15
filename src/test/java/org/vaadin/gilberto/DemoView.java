package org.vaadin.gilberto;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.router.Route;

@Route("")
public class DemoView extends Div {

    private CameraCapture camera;
    private Button togglePlay;
    private Button capture;
    private Image image;

    public DemoView() {
        camera = new CameraCapture("480px", "320px");
        camera.setOverlay(new OverlayStyle().setLeft("90px").setRight("90px")
                .setTop("10px").setBottom("10px").setBorderRadius("50%")
                .setBoxShadow("0 0 150px black").setBorder("2px solid black"));
        togglePlay = new Button("Stop", event -> {
            if (camera.isPlaying()) {
                event.getSource().setText("Play");
                camera.stop();
            } else {
                event.getSource().setText("Stop");
                camera.play();
            }
        });
        capture = new Button("Capture",
                event -> camera.capture(this::onCapture));

        image = new Image();
        image.getStyle().set("borderRadius", "50%");

        Div horizontal = new Div(camera, image);
        add(horizontal, togglePlay, capture);
    }

    private void onCapture(String data) {
        image.setSrc(data);
    }
}
