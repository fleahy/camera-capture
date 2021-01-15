import { PolymerElement } from '@polymer/polymer/polymer-element.js';
import { html } from '@polymer/polymer/lib/utils/html-tag.js';
class CameraCapture extends PolymerElement {
    static get template() {
        return html`
        <style>
            :host {
                position: relative;
                display: inline-block;
            }
            #video {
                transform: rotateY(180deg);
                object-fit: contain;
            }
            #overlay {
                position: absolute;
                box-sizing: border-box;
                top: var(--overlay-top);
                bottom: var(--overlay-bottom);
                left: var(--overlay-left);
                right: var(--overlay-right);
                border-radius: var(--overlay-border-radius);
                border: var(--overlay-border);
                box-shadow: var(--overlay-box-shadow);
            }
        </style>
        <video id="video" width$="[[width]]" height$="[[height]]"></video>
        <div id="overlay" hidden$="[[overlayHidden]]"></div>
    `;
}

static get is() {
    return 'camera-capture'
}
static get properties() {
    return {
        width: {
            type: String,
            value: '640px',
            notify: true
            },
        height: {
            type: String,
            value: '480px',
            notify: true
            },
        overlayHidden: {
            type: Boolean,
            value: true,
            notify: true
        },
        overlayStyle: {
            type: Object,
            observer: '_onOverlayStyleChanged'
        }
    }
}

play() {
    this.stop();
    const videoElement = this.$.video;
    
    navigator.mediaDevices.getUserMedia({
        video: true,
        audio: false
        }).then(stream => {
        videoElement.srcObject = stream;
        videoElement.captureStream = videoElement.captureStream || videoElement.mozCaptureStream;
        videoElement.play();
        }).catch(err => console.error(err));
}

stop() {
    const videoElement = this.$.video;
    if (!videoElement.srcObject) {
        return;
    }
    videoElement.srcObject.getTracks().forEach(track => track.stop());
}

pause() {
    const videoElement = this.$.video;
    videoElement.pause();
}

capture(mimeType) {
    if (!mimeType) {
        mimeType = "image/png";
    }
    
    const videoElement = this.$.video;
    const overlay = this.$.overlay;
    const canvas = document.createElement('canvas');
    const context = canvas.getContext("2d");
    const videoDimensions = this._getVideoDimensions();
                    
    if (!this.overlayHidden){
        let width = overlay.clientWidth;
        let height = overlay.clientHeight;
        
        canvas.width = width;
        canvas.height = height;
        context.translate(width, 0);
        context.scale(-1, 1);
        let sx = overlay.offsetLeft * videoDimensions.width / width;
        let sy = overlay.offsetTop * videoDimensions.height / height;
        context.drawImage(videoElement, 
                sx, sy, 
                width * videoDimensions.elementRatio, 
                height * videoDimensions.elementRatio,
                0, 0, width, height);
    } else {
        let width = videoDimensions.width;
        let height = videoDimensions.height;
        
        canvas.width = width;
        canvas.height = height;
        context.translate(width, 0);
        context.scale(-1, 1);
        context.drawImage(videoElement, 0, 0, width, height);
    }
    
    return canvas.toDataURL(mimeType);
}

_captureAndSendToServer(captureId, mimeType) {
    let data = this.capture(mimeType);
    this.$server.onImageCaptured(captureId, data);
}

_onOverlayStyleChanged(value) {
    this.updateStyles({
        '--overlay-top': value.top,
        '--overlay-bottom': value.bottom,
        '--overlay-left': value.left,
        '--overlay-right': value.right,
        '--overlay-border': value.border,
        '--overlay-border-radius': value.borderRadius,
        '--overlay-box-shadow': value.boxShadow
        });
}

_getVideoDimensions() {
    const video = this.$.video;
    // Ratio of the video's intrisic dimensions
    let videoRatio = video.videoWidth / video.videoHeight;
    // The width and height of the video element
    let width = video.offsetWidth, height = video.offsetHeight;
    // The ratio of the element's width to its height
    let elementRatio = width/height;
    // If the video element is short and wide
    if(elementRatio > videoRatio) width = height * videoRatio;
    // It must be tall and thin, or exactly equal to the original ratio
    else height = width / videoRatio;
    return {
        width: width,
        height: height,
        videoRatio: videoRatio,
        elementRatio: elementRatio
    };
    }

}

customElements.define(CameraCapture.is, CameraCapture);