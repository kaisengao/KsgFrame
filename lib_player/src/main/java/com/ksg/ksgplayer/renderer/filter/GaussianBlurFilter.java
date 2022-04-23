package com.ksg.ksgplayer.renderer.filter;

/**
 * @ClassName: GLGaussianBlurFilter
 * @Author: KaiSenGao
 * @CreateDate: 2022/4/23 14:40
 * @Description:
 */
public class GaussianBlurFilter implements GLFilter {

    /**
     * 偏移量
     */
    private final float mRadius;

    /**
     * XY方向
     */
    private final int mBlurX, mBlurY;

    /**
     * 透明度
     */
    private final float mTrans;

    public GaussianBlurFilter() {
        this(4f, 5, 5, 0.005f);
    }

    public GaussianBlurFilter(float radius, int blurX, int blurY, float trans) {
        this.mRadius = radius;
        this.mBlurX = blurX;
        this.mBlurY = blurY;
        this.mTrans = trans;
    }

    @Override
    public String getVertexShader() {
        return null;
    }

    @Override
    public String getFragmentShader() {
        return "#extension GL_OES_EGL_image_external : require\n" +
                "precision mediump float;\n" +
                "varying vec2 vTextureCoord;\n" +
                "uniform samplerExternalOES sTexture;\n" +
                "const float resolution = 1024.0;\n" +
                "const float radius = " + mRadius + ";\n" +
                "vec2 dir = vec2(1.0,1.0);\n" +
                "void main() {\n" +
                "   highp vec4 centralColor = vec4(0.0);\n" +
                "   float blur = radius/resolution;\n" +
                "   float hstep = dir.x;\n" +
                "   float vstep = dir.y;\n" +
                "   int x = " + mBlurX + ";int y = " + mBlurY + ";\n" +
                "   for(int i = x; i > 0; i--){\n" +
                "       for(int j = y; j > 0; j--){\n" +
                "           centralColor += texture2D(sTexture, vec2(vTextureCoord.x + float(i)*blur*hstep, vTextureCoord.y +float(j)*blur*vstep))*" + mTrans + ";" +
                "           centralColor += texture2D(sTexture, vec2(vTextureCoord.x - float(i)*blur*hstep, vTextureCoord.y +float(j)*blur*vstep))*" + mTrans + ";" +
                "           centralColor += texture2D(sTexture, vec2(vTextureCoord.x - float(i)*blur*hstep, vTextureCoord.y -float(j)*blur*vstep))*" + mTrans + ";" +
                "           centralColor += texture2D(sTexture, vec2(vTextureCoord.x + float(i)*blur*hstep, vTextureCoord.y -float(j)*blur*vstep))*" + mTrans + ";" +
                "       }\n" +
                "   }\n" +
                "   gl_FragColor = vec4(centralColor);\n" +
                "}";
    }
}
