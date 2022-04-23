package com.ksg.ksgplayer.renderer.filter;

/**
 * @ClassName: NoFilter
 * @Author: KaiSenGao
 * @CreateDate: 2022/4/23 21:09
 * @Description: 无滤镜
 */
public class NoFilter implements GLFilter {

    public static final String DEFAULT_VERTEX = "uniform mat4 uMVPMatrix;\n"
            + "uniform mat4 uSTMatrix;\n"
            + "attribute vec4 aPosition;\n"
            + "attribute vec4 aTextureCoord;\n"
            + "varying vec2 vTextureCoord;\n"
            + "void main() {\n"
            + "  gl_Position = uMVPMatrix * aPosition;\n"
            + "  vTextureCoord = (uSTMatrix * aTextureCoord).xy;\n"
            + "}\n";

    public static final String DEFAULT_FRAGMENT = "#extension GL_OES_EGL_image_external : require\n"
            + "precision mediump float;\n"
            + "varying vec2 vTextureCoord;\n"
            + "uniform samplerExternalOES sTexture;\n" + "void main() {\n"
            + "  gl_FragColor = texture2D(sTexture, vTextureCoord);\n"
            + "}\n";

    @Override
    public String getVertexShader() {
        return DEFAULT_VERTEX;
    }

    @Override
    public String getFragmentShader() {
        return DEFAULT_FRAGMENT;
    }
}